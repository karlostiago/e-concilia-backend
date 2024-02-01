package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.Ocorrencia;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.ConciliadorDTO;
import com.ctsousa.econcilia.model.dto.ResumoFinanceiroDTO;
import com.ctsousa.econcilia.model.dto.TotalizadorDTO;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.ConciliadorIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/conciliadores/ifood", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConciliadorIfoodResource {

    private final IntegracaoService integracaoService;

    private final ConciliadorIfoodService conciliadorIfoodService;

    public ConciliadorIfoodResource(IntegracaoService integracaoService, ConciliadorIfoodService conciliadorIfoodService) {
        this.integracaoService = integracaoService;
        this.conciliadorIfoodService = conciliadorIfoodService;
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_CONCILIADOR_IFOOD)
    public ResponseEntity<ConciliadorDTO> vendas(@RequestParam(name = "lojaId") final String lojaId,
                                                 @RequestParam(name = "dtInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtInicial,
                                                 @RequestParam(name = "dtFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtFinal,
                                                 @RequestParam(name = "metodoPagamento", required = false) final String metodoPagamento,
                                                 @RequestParam(name = "bandeira", required = false) final String bandeira,
                                                 @RequestParam(name = "tipoRecebimento", required = false) final String tipoRecebimento) {
        /**
         * <br>Para remover<br/>
        List<Venda> vendas = integracaoService.pesquisarVendasIfood(lojaId, metodoPagamento, bandeira, tipoRecebimento, dtInicial, dtFinal);
        conciliadorIfoodService.aplicarCancelamento(vendas, lojaId);
        conciliadorIfoodService.reprocessarVenda(dtInicial, dtFinal, lojaId, vendas);
        List<Ocorrencia> ocorrencias = integracaoService.pesquisarOcorrencias(lojaId, dtInicial, dtFinal);
        return ResponseEntity.ok(getConciliadorDTO(vendas, ocorrencias));
         */

        var conciliador = conciliadorIfoodService.conciliar(lojaId, metodoPagamento, bandeira, tipoRecebimento, dtInicial, dtFinal);
        return ResponseEntity.ok(conciliador);
    }

//    private ConciliadorDTO getConciliadorDTO(final List<Venda> vendas, final List<Ocorrencia> ocorrencias) {
//        ConciliadorDTO conciliadorDTO = new ConciliadorDTO();
//        conciliadorDTO.setVendas(vendas);
//        conciliadorDTO.setTotalizador(getTotalizadorDTO(vendas, ocorrencias));
//        conciliadorDTO.setResumoFinanceiro(getResumoFinanceiroDTO(vendas));
//        return conciliadorDTO;
//    }
//
//    private TotalizadorDTO getTotalizadorDTO(final List<Venda> vendas, final List<Ocorrencia> ocorrencias) {
//        return conciliadorIfoodService.totalizar(vendas, ocorrencias);
//    }
//
//    private ResumoFinanceiroDTO getResumoFinanceiroDTO(final List<Venda> vendas) {
//        return conciliadorIfoodService.calcularResumoFinanceiro(vendas);
//    }
}
