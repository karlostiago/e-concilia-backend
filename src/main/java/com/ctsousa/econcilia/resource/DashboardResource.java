package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaDTO;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaMeioPagamentoDTO;
import com.ctsousa.econcilia.service.DashboadService;
import com.ctsousa.econcilia.service.impl.GraficoVendaUltimo7DiaMeioPagamentoServiceImpl;
import com.ctsousa.econcilia.service.impl.GraficoVendaUltimo7DiaServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

    private final GraficoVendaUltimo7DiaMeioPagamentoServiceImpl graficoVendaUltimo7DiaMeioPagamentoService;

    private final DashboadService dashboadService;

    public DashboardResource(GraficoVendaUltimo7DiaServiceImpl graficoVendaUltimo7DiaService, DashboadService dashboadService, GraficoVendaUltimo7DiaMeioPagamentoServiceImpl graficoVendaUltimo7DiaMeioPagamentoService) {
        this.graficoVendaUltimo7DiaService = graficoVendaUltimo7DiaService;
        this.dashboadService = dashboadService;
        this.graficoVendaUltimo7DiaMeioPagamentoService = graficoVendaUltimo7DiaMeioPagamentoService;
    }

    @GetMapping
    public ResponseEntity<DashboardDTO> carregarInfo(@RequestParam(name = "lojaId", required = false) final Long empresaId,
                                                     @RequestParam(name = "dtInicial", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtInicial,
                                                     @RequestParam(name = "dtFinal", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtFinal) {
        DashboardDTO dashboardDTO = dashboadService.carregarInformacoes(empresaId,  dtInicial, dtFinal);
        return ResponseEntity.ok(dashboardDTO);
    }

    @GetMapping(value = "/buscar-venda-ultimos-7-dias")
    public ResponseEntity<GraficoVendaUltimo7DiaDTO> buscarVendasUltimos7Dias(@RequestParam(name = "lojaId", required = false) final Long empresaId) {
        List<Venda> vendas = dashboadService.buscarVendasUltimos7Dias(empresaId);
        return ResponseEntity.ok(graficoVendaUltimo7DiaService.processar(vendas));
    }

    @GetMapping(value = "/buscar-venda-ultimos-7-dias-meio-pagamento")
    public ResponseEntity<GraficoVendaUltimo7DiaMeioPagamentoDTO> buscarVendasUltimos7DiasMeioPagamento(@RequestParam(name = "lojaId", required = false) final Long empresaId) {
        List<Venda> vendas = dashboadService.buscarVendasUltimos7Dias(empresaId);
        return ResponseEntity.ok(graficoVendaUltimo7DiaMeioPagamentoService.processar(vendas));
    }
}
