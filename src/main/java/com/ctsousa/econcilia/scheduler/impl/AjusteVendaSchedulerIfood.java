package com.ctsousa.econcilia.scheduler.impl;

import com.ctsousa.econcilia.enumaration.TipoParametro;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.PeriodoDTO;
import com.ctsousa.econcilia.repository.OcorrenciaRepository;
import com.ctsousa.econcilia.repository.ParametroRepository;
import com.ctsousa.econcilia.repository.VendaRepository;
import com.ctsousa.econcilia.scheduler.Scheduler;
import com.ctsousa.econcilia.service.ContratoService;
import com.ctsousa.econcilia.service.IntegracaoIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import com.ctsousa.econcilia.service.OperadoraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.econcilia.util.DataUtil.obterPeriodoPorMesFechado;

@Slf4j
@Component
public class AjusteVendaSchedulerIfood implements Scheduler {

    private static final String IFOOD_OPERADORA = "ifood";

    private final OperadoraService operadoraService;

    private final ContratoService contratoService;

    private final ParametroRepository parametroRepository;

    private final VendaRepository vendaRepository;

    private final IntegracaoIfoodService integracaoIfoodService;

    private final IntegracaoService integracaoService;

    private final OcorrenciaRepository ocorrenciaRepository;

    public AjusteVendaSchedulerIfood(OperadoraService operadoraService, ContratoService contratoService, ParametroRepository parametroRepository, VendaRepository vendaRepository, IntegracaoIfoodService integracaoIfoodService, IntegracaoService integracaoService, OcorrenciaRepository ocorrenciaRepository) {
        this.operadoraService = operadoraService;
        this.contratoService = contratoService;
        this.parametroRepository = parametroRepository;
        this.vendaRepository = vendaRepository;
        this.integracaoIfoodService = integracaoIfoodService;
        this.integracaoService = integracaoService;
        this.ocorrenciaRepository = ocorrenciaRepository;
    }

    /**
     * Este processo sera executado a cada 50 minutos
     */
    @Override
    @Scheduled(cron = "0 */50 * * * *")
    public void processar() {
        Operadora operadora = operadoraService.buscarPorDescricao(IFOOD_OPERADORA);
        List<Contrato> contratos = contratoService.pesquisar(null, operadora.getId());

        List<Empresa> empresas = contratos.stream()
                .map(Contrato::getEmpresa)
                .toList();

        if (empresas.isEmpty()) return;

        log.info("Iniciando processo de atualização registro de vendas.");

        for (Empresa empresa : empresas) {
            Parametro parametro = parametroRepository.buscaParametroTipoEmpresaOperadora(tipoParametro(), empresa, operadora);
            if (parametro != null && parametro.isAtivo()) {
                prepararAtualizacao(empresa, operadora);
                log.info("Processo de ajuste de vendas concluída com sucesso.");
            } else {
                log.info("O processo de ajuste de vendas não está habilitado.");
            }
        }
    }

    private void prepararAtualizacao(Empresa empresa, Operadora operadora) {
        List<Integracao> integracaoes = integracaoService.pesquisar(empresa.getId(), operadora.getId(), null);
        for (Integracao integracao : integracaoes) {
            executarAtualizacaoVendas(integracao);
            executarAtualizacaoOcorrencias(integracao);
        }
    }

    private void executarAtualizacaoVendas(Integracao integracao) {
        List<Venda> vendas = vendaRepository.buscarVendasSemPeriodoId(integracao.getEmpresa(), integracao.getOperadora());

        if (vendas.isEmpty()) {
            log.info("::: Não foi encontrada venda para empresaId {}, operadoraId {} :::", integracao.getEmpresa().getId(), integracao.getOperadora().getId());
            return;
        }

        LocalDate dtInicial = vendas.get(0).getDataPedido();
        LocalDate dtFinal = vendas.get(vendas.size() - 1).getDataPedido();

        List<PeriodoDTO> periodos = obterPeriodoPorMesFechado(dtInicial, dtFinal);

        for (PeriodoDTO periodoDTO : periodos) {
            List<Venda> vendasIfood = integracaoIfoodService.pesquisarVendas(integracao.getCodigoIntegracao(), periodoDTO.getDe(), periodoDTO.getAte());

            if (vendasIfood.isEmpty()) continue;

            for (Venda venda : vendas) {
                if (venda.getPeriodoId() == null || venda.getPeriodoId().isEmpty()) {
                    atualizarVenda(vendasIfood, venda);
                }
            }
        }
    }

    private void atualizarVenda(List<Venda> vendas, Venda venda) {
        List<Venda> vendasFiltradas = vendas.stream()
                .filter(v -> v.getDataPedido().equals(venda.getDataPedido()))
                .toList();

        if (vendasFiltradas.isEmpty()) return;

        for (Venda vendaFiltrada : vendasFiltradas) {
            if (vendaFiltrada.getPeriodoId() == null || vendaFiltrada.getPeriodoId().isEmpty()) continue;

            if (vendaFiltrada.getPedidoId().equalsIgnoreCase(venda.getPedidoId())) {
               venda.setPeriodoId(vendaFiltrada.getPeriodoId());
               vendaRepository.save(venda);
               break;
           }
       }
    }

    private void executarAtualizacaoOcorrencias(Integracao integracao) {
        List<Ocorrencia> ocorrencias = ocorrenciaRepository.buscarOcorrenciaSemPeriodoId();

        if (ocorrencias.isEmpty()) {
            log.info("::: Não foi encontrada ocorrencias para empresaId {}, operadoraId {} :::", integracao.getEmpresa().getId(), integracao.getOperadora().getId());
            return;
        }

        LocalDate dtInicial = ocorrencias.get(0).getDataTransacao();
        LocalDate dtFinal = ocorrencias.get(ocorrencias.size() - 1).getDataTransacao();

        List<PeriodoDTO> periodos = obterPeriodoPorMesFechado(dtInicial, dtFinal);

        for (PeriodoDTO periodoDTO : periodos) {
            List<Ocorrencia> ocorrenciasIfood = integracaoIfoodService.pesquisarOcorrencias(integracao.getCodigoIntegracao(), periodoDTO.getDe(), periodoDTO.getAte());

            for (Ocorrencia ocorrencia : ocorrencias) {
                atualizarOcorrencia(ocorrenciasIfood, ocorrencia);
            }
        }
    }

    private void atualizarOcorrencia(List<Ocorrencia> ocorrencias, Ocorrencia ocorrencia) {
        List<Ocorrencia> ocorrenciasFiltradas = ocorrencias.stream()
                .filter(oc -> oc.getDataTransacao().equals(ocorrencia.getDataTransacao()))
                .toList();

        if (ocorrenciasFiltradas.isEmpty()) return;

        for (Ocorrencia ocorrenciaFiltrada : ocorrenciasFiltradas) {
            if (ocorrenciaFiltrada.getPeriodoId() == null || ocorrenciaFiltrada.getPeriodoId().isEmpty()) continue;

            if (ocorrenciaFiltrada.getTransacaoId().equalsIgnoreCase(ocorrencia.getTransacaoId())) {
                ocorrencia.setPeriodoId(ocorrenciaFiltrada.getPeriodoId());
                ocorrenciaRepository.save(ocorrencia);
                break;
            }
        }
    }

    @Override
    public TipoParametro tipoParametro() {
        return TipoParametro.AJUSTE_VENDA;
    }
}
