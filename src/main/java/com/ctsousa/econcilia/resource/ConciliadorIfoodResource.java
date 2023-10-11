package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.model.dto.ConciliadorDTO;
import com.ctsousa.econcilia.model.dto.ResumoFinanceiroDTO;
import com.ctsousa.econcilia.model.dto.TotalizadorDTO;
import com.ctsousa.econcilia.service.ConciliadorIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/conciliadores/ifood")
public class ConciliadorIfoodResource {

    private final IntegracaoService integracaoService;

    private final ConciliadorIfoodService conciliadorIfoodService;

    public ConciliadorIfoodResource(IntegracaoService integracaoService, ConciliadorIfoodService conciliadorIfoodService) {
        this.integracaoService = integracaoService;
        this.conciliadorIfoodService = conciliadorIfoodService;
    }

    @GetMapping
    public ResponseEntity<ConciliadorDTO> vendas(@RequestParam(name = "lojaId") final String lojaId,
                                                 @RequestParam(name = "dtInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtInicial,
                                                 @RequestParam(name = "dtFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtFinal,
                                                 @RequestParam(name  = "metodoPagamento", required = false) final String metodoPagamento,
                                                 @RequestParam(name  = "bandeira", required = false) final String bandeira) {

        List<Venda> vendas = integracaoService.pesquisarVendasIfood(lojaId, metodoPagamento, bandeira, dtInicial, dtFinal);
//        List<AjusteVenda> ajusteVendas = integracaoService.pesquisarAjusteVendasIfood(lojaId, dtInicial, dtFinal);
//        List<Pagamento> pagamentos = integracaoService.pesquisarPagamentos(lojaId, dtInicial, dtFinal);

        vendas = conciliadorIfoodService.conciliarTaxas(vendas, lojaId);

        TotalizadorDTO totalizadorDTO = conciliadorIfoodService.totalizar(vendas);
        ResumoFinanceiroDTO resumoFinanceiroDTO = conciliadorIfoodService.calcularResumoFinanceiro(vendas);

        ConciliadorDTO conciliadorDTO = new ConciliadorDTO();
        conciliadorDTO.setVendas(vendas);
        conciliadorDTO.setTotalizador(totalizadorDTO);
        conciliadorDTO.setResumoFinanceiro(resumoFinanceiroDTO);

        return ResponseEntity.ok(conciliadorDTO);
    }
}
