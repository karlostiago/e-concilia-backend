package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.assembler.impl.ContratoMapper;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.model.dto.TaxaDTO;
import com.ctsousa.econcilia.service.ContratoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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

    @GetMapping
    public ResponseEntity<List<ContratoDTO>> listar (@RequestParam( required = false ) Long empresaId, @RequestParam (required = false) Long operadoraId) {
        var contratos = this.contratoService.pesquisar(empresaId, operadoraId);
        return ResponseEntity.ok(contratoMapper.paraLista(contratos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoDTO> buscarPorId (@PathVariable Long id) {
        var contrato = this.contratoService.pesquisarPorId(id);
        return ResponseEntity.ok(contratoMapper.paraDTO(contrato));
    }

    @DeleteMapping("/{id}")
    public void deletar (@PathVariable Long id) {
        this.contratoService.deletar(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoDTO> atualizar (@PathVariable Long id, @RequestBody @Valid ContratoDTO contratoDTO) {
        var contrato = this.contratoService.atualizar(id, contratoDTO);
        return ResponseEntity.ok(contratoMapper.paraDTO(contrato));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ContratoDTO> ativar (@PathVariable Long id) {
        return ResponseEntity.ok(contratoMapper.paraDTO(contratoService.ativar(id)));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ContratoDTO> desativar (@PathVariable Long id) {
        return ResponseEntity.ok(contratoMapper.paraDTO(contratoService.desativar(id)));
    }
}
