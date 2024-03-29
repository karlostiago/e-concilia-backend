package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.ContratoMapper;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.ContratoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @PreAuthorize(Autorizar.CADASTRAR_CONTRATO)
    public ResponseEntity<ContratoDTO> cadastrar(@RequestBody @Valid ContratoDTO contratoDTO) {
        var contrato = contratoService.salvar(contratoMapper.paraEntidade(contratoDTO));
        return ResponseEntity.ok(contratoMapper.paraDTO(contrato));
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_CONTRATO)
    public ResponseEntity<List<ContratoDTO>> listar(@RequestParam(required = false) Long empresaId, @RequestParam(required = false) Long operadoraId) {
        var contratos = this.contratoService.pesquisar(empresaId, operadoraId);
        return ResponseEntity.ok(contratoMapper.paraLista(contratos));
    }

    @GetMapping("/{id}")
    @PreAuthorize(Autorizar.PESQUISAR_CONTRATO)
    public ResponseEntity<ContratoDTO> buscarPorId(@PathVariable Long id) {
        var contrato = this.contratoService.pesquisarPorId(id);
        return ResponseEntity.ok(contratoMapper.paraDTO(contrato));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Autorizar.DELETAR_CONTRATO)
    public void deletar(@PathVariable Long id) {
        this.contratoService.deletar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Autorizar.EDITAR_CONTRATO)
    public ResponseEntity<ContratoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ContratoDTO contratoDTO) {
        var contrato = this.contratoService.atualizar(id, contratoDTO);
        return ResponseEntity.ok(contratoMapper.paraDTO(contrato));
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize(Autorizar.ATIVAR_CONTRATO)
    public ResponseEntity<ContratoDTO> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(contratoMapper.paraDTO(contratoService.ativar(id)));
    }

    @PatchMapping("/{id}/desativar")
    @PreAuthorize(Autorizar.ATIVAR_CONTRATO)
    public ResponseEntity<ContratoDTO> desativar(@PathVariable Long id) {
        return ResponseEntity.ok(contratoMapper.paraDTO(contratoService.desativar(id)));
    }
}
