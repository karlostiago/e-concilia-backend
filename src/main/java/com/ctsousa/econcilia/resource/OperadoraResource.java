package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.OperadoraMapper;
import com.ctsousa.econcilia.model.dto.OperadoraDTO;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.OperadoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @PreAuthorize(Autorizar.CADASTRAR_OPERADORA)
    public ResponseEntity<OperadoraDTO> cadastrar (@RequestBody @Valid OperadoraDTO operadoraDTO) {
        var operadora = operadoraService.salvar(operadoraMapper.paraEntidade(operadoraDTO));
        return ResponseEntity.ok(operadoraMapper.paraDTO(operadora));
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_OPERADORA)
    public ResponseEntity<List<OperadoraDTO>> listar (@RequestParam( required = false ) String descricao) {
        var operadoras = operadoraService.pesquisar(descricao);
        return ResponseEntity.ok(operadoraMapper.paraLista(operadoras));
    }

    @GetMapping("/{id}")
    @PreAuthorize(Autorizar.PESQUISAR_OPERADORA)
    public ResponseEntity<OperadoraDTO> porId (@PathVariable Long id) {
        var operadora = operadoraService.buscarPorID(id);
        return ResponseEntity.ok(operadoraMapper.paraDTO(operadora));
    }

    @PutMapping("/{id}")
    @PreAuthorize(Autorizar.EDITAR_OPERADORA)
    public ResponseEntity<OperadoraDTO> atualizar (@PathVariable Long id, @RequestBody @Valid OperadoraDTO operadoraDTO) {
        var operadora = this.operadoraService.atualizar(id, operadoraDTO);
        return ResponseEntity.ok(operadoraMapper.paraDTO(operadora));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Autorizar.DELETAR_OPERADORA)
    public void deletar (@PathVariable Long id) {
        this.operadoraService.deletar(id);
    }
}
