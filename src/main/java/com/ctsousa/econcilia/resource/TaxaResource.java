package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.dto.TaxaDTO;
import com.ctsousa.econcilia.service.TaxaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/taxas")
public class TaxaResource {

    private final TaxaService taxaService;

    public TaxaResource(TaxaService taxaService) {
        this.taxaService = taxaService;
    }

    @PostMapping("/validar")
    public ResponseEntity<TaxaDTO> validar (@RequestBody @Valid TaxaDTO taxaDTO) {
        this.taxaService.validar(taxaDTO);
        taxaDTO.setExpiraEm(this.taxaService.calcularTempoExpiracao(taxaDTO.getEntraEmVigor(), taxaDTO.getValidoAte()));
        return ResponseEntity.ok(taxaDTO);
    }
}
