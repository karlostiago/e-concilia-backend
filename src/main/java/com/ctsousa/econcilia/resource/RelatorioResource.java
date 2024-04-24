package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.enumaration.TipoRelatorio;
import com.ctsousa.econcilia.filter.RelatorioFilter;
import com.ctsousa.econcilia.service.ConsolidacaoService;
import com.ctsousa.econcilia.service.GeradorRelatorioCSVService;
import com.ctsousa.econcilia.service.TaxaService;
import com.ctsousa.econcilia.service.VendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/relatorios")
public class RelatorioResource {

    private final TaxaService taxaService;

    private final VendaService vendaService;

    private final ConsolidacaoService consolidacaoService;

    public RelatorioResource(TaxaService taxaService, VendaService vendaService, ConsolidacaoService consolidacaoService) {
        this.taxaService = taxaService;
        this.vendaService = vendaService;
        this.consolidacaoService = consolidacaoService;
    }

    @GetMapping(value = "/gerar/csv/taxas")
    public ResponseEntity<byte[]> gerarCSVTaxas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                        @RequestParam Long empresaId,
                                        @RequestParam Long operadoraId,
                                        @RequestParam String tipo) {

        byte [] bytes = gerarBytes(taxaService, dataInicial, dataFinal, empresaId, operadoraId, tipo);

        return ResponseEntity.ok()
                .headers(getHeaders())
                .body(bytes);
    }

    @GetMapping(value = "/gerar/csv/vendas")
    public ResponseEntity<byte[]> gerarCSVVendas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                                @RequestParam Long empresaId,
                                                @RequestParam Long operadoraId,
                                                @RequestParam String tipo) {

        byte [] bytes = gerarBytes(vendaService, dataInicial, dataFinal, empresaId, operadoraId, tipo);

        return ResponseEntity.ok()
                .headers(getHeaders())
                .body(bytes);
    }

    @GetMapping(value = "/gerar/csv/conciliacao")
    public ResponseEntity<byte[]> gerarCSVConciliacao(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                                 @RequestParam Long empresaId,
                                                 @RequestParam Long operadoraId,
                                                 @RequestParam String tipo) {

        byte [] bytes = gerarBytes(consolidacaoService, dataInicial, dataFinal, empresaId, operadoraId, tipo);

        return ResponseEntity.ok()
                .headers(getHeaders())
                .body(bytes);
    }

    private byte [] gerarBytes(GeradorRelatorioCSVService service, LocalDate dataInicial, LocalDate dataFinal, Long empresaId, Long operadoraId, String tipo) {
        byte [] bytes = {};

        TipoRelatorio tipoRelatorio = TipoRelatorio.porDescricao(tipo);
        RelatorioFilter filtro = new RelatorioFilter(dataInicial, dataFinal, empresaId, operadoraId, tipoRelatorio);

        if (tipoRelatorio != null)
            bytes = tipoRelatorio.gerar(service, filtro);

        return bytes;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Relatorio.csv");
        return headers;
    }
}
