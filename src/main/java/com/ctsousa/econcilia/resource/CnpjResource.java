package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.CancelamentoMapper;
import com.ctsousa.econcilia.mapper.impl.CnpjMapper;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cnpj")
public class CnpjResource {

    private final CancelamentoMapper.CnpjService cnpjService;

    private final CnpjMapper cnpjMapper;

    public CnpjResource(CancelamentoMapper.CnpjService cnpjService, CnpjMapper cnpjMapper) {
        this.cnpjService = cnpjService;
        this.cnpjMapper = cnpjMapper;
    }

    @GetMapping("{cnpj}")
    public ResponseEntity<EmpresaDTO> porCnpj(@PathVariable String cnpj) {
        var dadosCnpj = cnpjService.buscarCNPJ(cnpj);
        return ResponseEntity.ok(cnpjMapper.paraDTO(dadosCnpj));
    }
}