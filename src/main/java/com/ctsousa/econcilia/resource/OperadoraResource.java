package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.assembler.impl.OperadoraMapper;
import com.ctsousa.econcilia.model.dto.OperadoraDTO;
import com.ctsousa.econcilia.service.OperadoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/operadoras")
public class OperadoraResource {
    private final OperadoraService operadoraService;
    private final OperadoraMapper operadoraMapper;
    public OperadoraResource(OperadoraService operadoraService, OperadoraMapper operadoraAssembler) {
        this.operadoraService = operadoraService;
        this.operadoraMapper = operadoraAssembler;
    }
    @PostMapping
    public ResponseEntity<OperadoraDTO> cadastrar (@RequestBody @Valid OperadoraDTO operadoraDTO) {
        var operadora = operadoraService.salvar(operadoraMapper.paraEntidade(operadoraDTO));
        return ResponseEntity.ok(operadoraMapper.paraDTO(operadora));
    }
}
