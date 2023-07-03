package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.assembler.impl.ContratoMapper;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import com.ctsousa.econcilia.service.ContratoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/contratos")
public class ContratoResource {

    private final ContratoService contratoService;
    private final ContratoMapper contratoMapper;

    public ContratoResource(ContratoService contratoService, ContratoMapper contratoMapper) {
        this.contratoService = contratoService;
        this.contratoMapper = contratoMapper;
    }

    @PostMapping
    public ResponseEntity<ContratoDTO> cadastrar (@RequestBody @Valid ContratoDTO contratoDTO) {
        var contrato = contratoService.salvar(contratoMapper.paraEntidade(contratoDTO));
        return ResponseEntity.ok(contratoMapper.paraDTO(contrato));
    }
}
