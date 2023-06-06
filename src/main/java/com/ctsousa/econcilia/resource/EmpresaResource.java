package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.service.EmpresaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas")
public class EmpresaResource {

    private final EmpresaService empresaService;

    EmpresaResource(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public void cadastrar (@RequestBody EmpresaDTO empresaDTO) {
        System.out.println("Servidor rodando...." + empresaDTO);
    }
}
