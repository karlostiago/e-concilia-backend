package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.assembler.EmpresaAssembler;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.service.CnpjService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("cnpj")
public class CnpjResource {

    private final CnpjService cnpjService;

    private final EmpresaAssembler empresaAssembler;

    public CnpjResource(CnpjService cnpjService, EmpresaAssembler empresaAssembler) {
        this.cnpjService = cnpjService;
        this.empresaAssembler = empresaAssembler;
    }

    @GetMapping("{cnpj}")
    public ResponseEntity<EmpresaDTO> porCnpj (@PathVariable String cnpj) {
        var dadosCnpj = cnpjService.buscarCNPJ(cnpj);
        return ResponseEntity.ok(empresaAssembler.paraDTO(dadosCnpj));
    }
}
