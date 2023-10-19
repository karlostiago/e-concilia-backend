package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.dto.DashboardDTO;
import com.ctsousa.econcilia.service.DashboadService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DashboardDTO> carregarInfo(@RequestParam(name = "lojaId") final Long empresaId,
                                                     @RequestParam(name = "dtInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtInicial,
                                                     @RequestParam(name = "dtFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtFinal) {

        DashboardDTO dashboardDTO = dashboadService.carregarInformacoes(empresaId,  dtInicial, dtFinal);

        return ResponseEntity.ok(dashboardDTO);
    }
}
