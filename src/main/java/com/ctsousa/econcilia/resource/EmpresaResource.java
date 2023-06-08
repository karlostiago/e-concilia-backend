package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.assembler.EmpresaAssembler;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.service.EmpresaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas")
public class EmpresaResource {

    private final EmpresaService empresaService;

    private final EmpresaAssembler empresaAssembler;

    EmpresaResource(EmpresaService empresaService, EmpresaAssembler empresaAssembler) {
        this.empresaService = empresaService;
        this.empresaAssembler = empresaAssembler;
    }

    @PostMapping
    public ResponseEntity<EmpresaDTO> cadastrar (@RequestBody EmpresaDTO empresaDTO) {
        var empresa = this.empresaService.salvar(empresaAssembler.paraEntidade(empresaDTO));
        return ResponseEntity.ok(empresaAssembler.paraDTO(empresa));
    }
}
