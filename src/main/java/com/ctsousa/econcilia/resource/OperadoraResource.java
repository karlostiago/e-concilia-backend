package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.OperadoraMapper;
import com.ctsousa.econcilia.model.dto.OperadoraDTO;
import com.ctsousa.econcilia.service.OperadoraService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<OperadoraDTO> cadastrar (@RequestBody @Valid OperadoraDTO operadoraDTO) {
        var operadora = operadoraService.salvar(operadoraMapper.paraEntidade(operadoraDTO));
        return ResponseEntity.ok(operadoraMapper.paraDTO(operadora));
    }

    @GetMapping
    public ResponseEntity<List<OperadoraDTO>> listar (@RequestParam( required = false ) String descricao) {
        var operadoras = operadoraService.pesquisar(descricao);
        return ResponseEntity.ok(operadoraMapper.paraLista(operadoras));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperadoraDTO> porId (@PathVariable Long id) {
        var operadora = operadoraService.buscarPorID(id);
        return ResponseEntity.ok(operadoraMapper.paraDTO(operadora));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperadoraDTO> atualizar (@PathVariable Long id, @RequestBody @Valid OperadoraDTO operadoraDTO) {
        var operadora = this.operadoraService.atualizar(id, operadoraDTO);
        return ResponseEntity.ok(operadoraMapper.paraDTO(operadora));
    }

    @DeleteMapping("/{id}")
    public void deletar (@PathVariable Long id) {
        this.operadoraService.deletar(id);
    }
}
