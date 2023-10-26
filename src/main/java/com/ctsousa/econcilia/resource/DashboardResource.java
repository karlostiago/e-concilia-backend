package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.GraficoVendaUltimo7DiaMapper;
import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.model.dto.GraficoVendaUltimo7DiaDTO;
import com.ctsousa.econcilia.service.DashboadService;
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

    private final GraficoVendaUltimo7DiaMapper graficoVendaUltimo7DiaMapper;

    private final DashboadService dashboadService;

    public DashboardResource(GraficoVendaUltimo7DiaMapper graficoVendaUltimo7DiaMapper, DashboadService dashboadService) {
        this.graficoVendaUltimo7DiaMapper = graficoVendaUltimo7DiaMapper;
        this.dashboadService = dashboadService;
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
        return ResponseEntity.ok(graficoVendaUltimo7DiaMapper.toDTO(vendas));
    }
}
