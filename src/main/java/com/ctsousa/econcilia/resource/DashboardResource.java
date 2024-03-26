package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.*;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.DashboadService;
import com.ctsousa.econcilia.service.impl.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardResource {

    private final GraficoVendaUltimo7DiaServiceImpl graficoVendaUltimo7DiaService;

    private final GraficoVendaUltimo7DiaDinheiroPixServiceImpl graficoVendaUltimo7DiaDinheiroPixService;

    private final GraficoVendaUltimo7DiaCreditoDebitoServiceImpl graficoVendaUltimo7DiaCreditoDebitoService;

    private final GraficoPercentualVendaFormaPagamentoImpl graficoPercentualVendaFormaPagamento;

    private final GraficoVendaMensalImpl graficoVendaMensal;

    private final GraficoVendaAnualImpl graficoVendaAnual;

    private final DashboadService dashboadService;

    private List<Venda> vendas;

    private List<Venda> vendasFiltradas;

    private LocalDate dtInicial;

    private LocalDate dtFinal;

    public DashboardResource(GraficoVendaUltimo7DiaServiceImpl graficoVendaUltimo7DiaService, GraficoVendaUltimo7DiaDinheiroPixServiceImpl graficoVendaUltimo7DiaDinheiroPixService,
                             GraficoVendaUltimo7DiaCreditoDebitoServiceImpl graficoVendaUltimo7DiaCreditoDebitoService, DashboadService dashboadService,
                             GraficoPercentualVendaFormaPagamentoImpl graficoPercentualVendaFormaPagamento,
                             GraficoVendaMensalImpl graficoVendaMensal,
                             GraficoVendaAnualImpl graficoVendaAnual) {
        this.graficoVendaUltimo7DiaService = graficoVendaUltimo7DiaService;
        this.graficoVendaUltimo7DiaDinheiroPixService = graficoVendaUltimo7DiaDinheiroPixService;
        this.graficoVendaUltimo7DiaCreditoDebitoService = graficoVendaUltimo7DiaCreditoDebitoService;
        this.dashboadService = dashboadService;
        this.graficoPercentualVendaFormaPagamento = graficoPercentualVendaFormaPagamento;
        this.graficoVendaMensal = graficoVendaMensal;
        this.graficoVendaAnual = graficoVendaAnual;
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_DASHBOARD)
    public ResponseEntity<DashboardDTO> carregarInfo(@RequestParam(name = "lojaId", required = false) final String empresaId,
                                                     @RequestParam(name = "dtInicial", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtInicial,
                                                     @RequestParam(name = "dtFinal", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtFinal) {

        DashboardDTO dashboardDTO = dashboadService.carregarInformacoes(empresaId, dtInicial, dtFinal);

        this.dtInicial = dtInicial;
        this.dtFinal = dtFinal;

        vendas = dashboardDTO.getVendas();

        if (vendas != null && !vendas.isEmpty()) {
            filtrarVendas();
        }

        return ResponseEntity.ok(dashboardDTO);
    }

    @GetMapping(value = "/buscar-venda-ultimos-7-dias")
    @PreAuthorize(Autorizar.PESQUISAR_DASHBOARD)
    public ResponseEntity<GraficoVendaUltimo7DiaDTO> buscarVendasUltimos7Dias() {
        if (vendasFiltradas == null) return ResponseEntity.ok().build();

        return ResponseEntity.ok(graficoVendaUltimo7DiaService.processar(vendasFiltradas));
    }

    @GetMapping(value = "/buscar-venda-ultimos-7-dias-credito-debito")
    @PreAuthorize(Autorizar.PESQUISAR_DASHBOARD)
    public ResponseEntity<GraficoVendaUltimo7DiaCreditoDebitoDTO> buscarVendasUltimos7DiasCreditoDebito() {
        if (vendasFiltradas == null) return ResponseEntity.ok().build();

        return ResponseEntity.ok(graficoVendaUltimo7DiaCreditoDebitoService.processar(vendasFiltradas));
    }

    @GetMapping(value = "/buscar-percentual-venda-forma-pagamento")
    @PreAuthorize(Autorizar.PESQUISAR_DASHBOARD)
    public ResponseEntity<GraficoPercentualVendaFormaPagamentoDTO> buscarPercentualVendasFormaPagamento() {
        if (vendasFiltradas == null) return ResponseEntity.ok().build();

        return ResponseEntity.ok(graficoPercentualVendaFormaPagamento.processar(vendasFiltradas));
    }

    @GetMapping(value = "/buscar-venda-ultimos-7-dias-dinheito-pix")
    @PreAuthorize(Autorizar.PESQUISAR_DASHBOARD)
    public ResponseEntity<GraficoVendaUltimo7DiaDinheiroPixDTO> buscarVendasUltimos7DiasDinheiroPix() {
        if (vendasFiltradas == null) return ResponseEntity.ok().build();

        return ResponseEntity.ok(graficoVendaUltimo7DiaDinheiroPixService.processar(vendasFiltradas));
    }

    @GetMapping(value = "/buscar-venda-mensal")
    @PreAuthorize(Autorizar.PESQUISAR_DASHBOARD)
    public ResponseEntity<GraficoVendaMensalDTO> buscarVendasMensal() {
        if (vendasFiltradas == null) return ResponseEntity.ok().build();

        return ResponseEntity.ok(graficoVendaMensal.processar(vendasFiltradas));
    }

    @GetMapping(value = "/buscar-venda-anual")
    @PreAuthorize(Autorizar.PESQUISAR_DASHBOARD)
    public ResponseEntity<GraficoVendaAnualDTO> buscarVendasAnual() {
        if (vendas == null) return ResponseEntity.ok().build();

        return ResponseEntity.ok(graficoVendaAnual.processar(vendas));
    }

    private void filtrarVendas() {
        vendasFiltradas = vendas.stream()
                .filter(venda -> !venda.getDataPedido().isBefore(this.dtInicial) && !venda.getDataPedido().isAfter(this.dtFinal))
                .toList();
    }
}
