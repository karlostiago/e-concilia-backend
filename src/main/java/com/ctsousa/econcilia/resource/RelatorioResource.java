package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.report.dto.RelatorioConsolidadoDTO;
import com.ctsousa.econcilia.report.dto.RelatorioTaxaDTO;
import com.ctsousa.econcilia.report.dto.RelatorioVendaDTO;
import com.ctsousa.econcilia.service.ConsolidacaoService;
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
import java.util.List;

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
                                        @RequestParam Long operadoraId) {

        Empresa empresa = new Empresa(empresaId);
        Operadora operadora = new Operadora(operadoraId);
        byte [] bytes = taxaService.gerarDadosCSV(dataInicial, dataFinal, empresa, operadora);

        return ResponseEntity.ok()
                .headers(getHeaders())
                .body(bytes);
    }

    @GetMapping(value = "/gerar/pdf/taxas")
    public ResponseEntity<List<RelatorioTaxaDTO>> gerarPDFTaxas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                                @RequestParam Long empresaId,
                                                @RequestParam Long operadoraId) {

        Empresa empresa = new Empresa(empresaId);
        Operadora operadora = new Operadora(operadoraId);
        List<RelatorioTaxaDTO> taxasDTO = taxaService.gerarDadosPDF(dataInicial, dataFinal, empresa, operadora);
        return ResponseEntity.ok(taxasDTO);
    }

    @GetMapping(value = "/gerar/csv/vendas")
    public ResponseEntity<byte[]> gerarCSVVendas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                                @RequestParam Long empresaId,
                                                @RequestParam Long operadoraId) {

        Empresa empresa = new Empresa(empresaId);
        Operadora operadora = new Operadora(operadoraId);
        byte [] bytes = vendaService.gerarDadosCSV(dataInicial, dataFinal, empresa, operadora);

        return ResponseEntity.ok()
                .headers(getHeaders())
                .body(bytes);
    }

    @GetMapping(value = "/gerar/pdf/vendas")
    public ResponseEntity<List<RelatorioVendaDTO>> gerarPDFVendas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                                                 @RequestParam Long empresaId,
                                                                 @RequestParam Long operadoraId) {

        Empresa empresa = new Empresa(empresaId);
        Operadora operadora = new Operadora(operadoraId);
        List<RelatorioVendaDTO> vendasDTO = vendaService.gerarDadosPDF(dataInicial, dataFinal, empresa, operadora);
        return ResponseEntity.ok(vendasDTO);
    }

    @GetMapping(value = "/gerar/csv/conciliacao")
    public ResponseEntity<byte[]> gerarCSVConciliacao(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                                 @RequestParam Long empresaId,
                                                 @RequestParam Long operadoraId) {

        Empresa empresa = new Empresa(empresaId);
        Operadora operadora = new Operadora(operadoraId);
        byte [] bytes = consolidacaoService.gerarDadosCSV(dataInicial, dataFinal, empresa, operadora);

        return ResponseEntity.ok()
                .headers(getHeaders())
                .body(bytes);
    }

    @GetMapping(value = "/gerar/pdf/conciliacao")
    public ResponseEntity<List<RelatorioConsolidadoDTO>> gerarPDFConciliacao(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                                                             @RequestParam Long empresaId,
                                                                             @RequestParam Long operadoraId) {
        Empresa empresa = new Empresa(empresaId);
        Operadora operadora = new Operadora(operadoraId);
        List<RelatorioConsolidadoDTO> consolidadoDTOS = consolidacaoService.gerarDadosPDF(dataInicial, dataFinal, empresa, operadora);
        return ResponseEntity.ok(consolidadoDTOS);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Relatorio.csv");
        return headers;
    }
}
