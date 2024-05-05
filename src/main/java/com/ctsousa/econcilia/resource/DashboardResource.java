package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.DashboadService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard")
public class DashboardResource {

    private final DashboadService dashboadService;

    public DashboardResource(DashboadService dashboadService) {
        this.dashboadService = dashboadService;
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_DASHBOARD)
    public ResponseEntity<DashboardDTO> carregarInfo(@RequestParam(name = "lojaId", required = false) final String empresaId,
                                                     @RequestParam(name = "dtInicial", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtInicial,
                                                     @RequestParam(name = "dtFinal", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtFinal) {

        DashboardDTO dashboardDTO = dashboadService.carregaVendasConsolidadas(empresaId, dtInicial, dtFinal);
        return ResponseEntity.ok(dashboardDTO);
    }
}
