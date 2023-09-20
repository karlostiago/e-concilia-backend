package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.service.ConciliadorIfoodService;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<Venda>> vendas(@RequestParam(name = "lojaId") final String lojaId, @RequestParam(name = "dtInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtInicial, @RequestParam(name = "dtFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtFinal) {
        List<Venda> vendas = integracaoService.pesquisarVendasIfood(lojaId, dtInicial, dtFinal);
        vendas = conciliadorIfoodService.conciliarTaxas(vendas, lojaId);
        return ResponseEntity.ok(vendas);
    }
}
