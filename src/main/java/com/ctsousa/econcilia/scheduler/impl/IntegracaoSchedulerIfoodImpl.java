package com.ctsousa.econcilia.scheduler.impl;

import com.ctsousa.econcilia.integration.ifood.entity.Merchant;
import com.ctsousa.econcilia.integration.ifood.gateway.IfoodGateway;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.IntegracaoBuffer;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.repository.EmpresaRepository;
import com.ctsousa.econcilia.repository.IntegracaoBufferRepository;
import com.ctsousa.econcilia.repository.IntegracaoRepository;
import com.ctsousa.econcilia.scheduler.Scheduler;
import com.ctsousa.econcilia.service.OperadoraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ctsousa.econcilia.util.StringUtil.removeCaracteresEspeciais;

@Slf4j
@Component
public class IntegracaoSchedulerIfoodImpl implements Scheduler {

    private static final String IFOOD_OPERADORA = "ifood";

    private static final long DOIS_MINUTOS = 120000L;

    private final IntegracaoBufferRepository integracaoBufferRepository;

    private final OperadoraService operadoraService;

    private final EmpresaRepository empresaRepository;

    private final IfoodGateway ifoodGateway;

    private final IntegracaoRepository integracaoRepository;

    public IntegracaoSchedulerIfoodImpl(IntegracaoBufferRepository integracaoBufferRepository, OperadoraService operadoraService, EmpresaRepository empresaRepository, IfoodGateway ifoodGateway, IntegracaoRepository integracaoRepository) {
        this.integracaoBufferRepository = integracaoBufferRepository;
        this.operadoraService = operadoraService;
        this.empresaRepository = empresaRepository;
        this.ifoodGateway = ifoodGateway;
        this.integracaoRepository = integracaoRepository;
    }

    @Override
    @Scheduled(fixedRate = DOIS_MINUTOS)
    public void processar() {
        List<IntegracaoBuffer> buffers = integracaoBufferRepository.findAll()
                .stream().filter(b -> b.getNomeOperadora().equalsIgnoreCase(IFOOD_OPERADORA))
                .toList();

        if (buffers.isEmpty()) return;

        log.info("Iniciando processo de integração automática, foram encontrado.:: {}, para serem integrados", buffers.size());

        for (IntegracaoBuffer buffer : buffers) {
            Empresa empresa = empresaRepository.porCnpj(buffer.getCnpj());
            Operadora operadora = operadoraService.buscarPorDescricao(IFOOD_OPERADORA);

            if (empresa != null) {
                integrar(empresa, operadora);
                integracaoBufferRepository.deleteById(buffer.getId());
            }
        }

        log.info("Processo de integração automática finalizado.:::");
    }

    private void integrar(final Empresa empresa, final Operadora operadora) {
        List<Merchant> merchants = ifoodGateway.findAllMerchants();

        for (Merchant merchant : merchants) {
            boolean existeIntegracao = integracaoRepository.existsByOperadoraAndCodigoIntegracao(operadora,merchant.getId());

            if (existeIntegracao) {
                log.info("Já tem uma integração para empresa.:: {} e operadora .:: {}?", empresa.getRazaoSocial(), operadora.getDescricao());
            }

            if ((removeCaracteresEspeciais(merchant.getName()).equalsIgnoreCase(empresa.getNomeFantasia())
                    && removeCaracteresEspeciais(merchant.getCorporateName()).equalsIgnoreCase(empresa.getRazaoSocial())) && !existeIntegracao) {
                log.info("Integrando empresa.:: {} e operadora.:: {}.", operadora.getDescricao(), operadora.getDescricao());
                salvarIntegracao(empresa, operadora, merchant.getId());
                break;
            }
        }
    }

    private void salvarIntegracao(final Empresa empresa, final Operadora operadora, final String codigo) {
        Integracao integracao = new Integracao();
        integracao.setEmpresa(empresa);
        integracao.setOperadora(operadora);
        integracao.setCodigoIntegracao(codigo);
        integracaoRepository.save(integracao);

        log.info("Integração efetivada com sucesso.");
    }
}
