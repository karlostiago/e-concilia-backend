package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.assembler.impl.VinculaEmpresaOperadoraMapper;
import com.ctsousa.econcilia.model.dto.ContratoDTO;
import com.ctsousa.econcilia.model.dto.VinculaEmpresaOperadoraDTO;
import com.ctsousa.econcilia.service.VinculaEmpresaOperadoraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/configuracao/vincula-empresa-operadora")
public class VinculaEmpresaOperadoraResource {

    private final VinculaEmpresaOperadoraService vinculaEmpresaOperadoraService;

    private final VinculaEmpresaOperadoraMapper vinculaEmpresaOperadoraMapper;

    public VinculaEmpresaOperadoraResource(VinculaEmpresaOperadoraService vinculaEmpresaOperadoraService, VinculaEmpresaOperadoraMapper vinculaEmpresaOperadoraMapper) {
        this.vinculaEmpresaOperadoraService = vinculaEmpresaOperadoraService;
        this.vinculaEmpresaOperadoraMapper = vinculaEmpresaOperadoraMapper;
    }

    @PostMapping
    public ResponseEntity<VinculaEmpresaOperadoraDTO> cadastrar (@RequestBody @Valid VinculaEmpresaOperadoraDTO vinculaEmpresaOperadoraDTO) {
        var vinculaEmpresaOperadora = vinculaEmpresaOperadoraService.salvar(vinculaEmpresaOperadoraMapper.paraEntidade(vinculaEmpresaOperadoraDTO));
        return ResponseEntity.ok(vinculaEmpresaOperadoraMapper.paraDTO(vinculaEmpresaOperadora));
    }

    @GetMapping
    public ResponseEntity<List<VinculaEmpresaOperadoraDTO>> listar (@RequestParam(required = false) Long empresaId, @RequestParam(required = false) Long operadoraId, @RequestParam(required = false) String codigoIntegracao) {
        var vinculoEmpresaOperadoraList = vinculaEmpresaOperadoraService.pesquisar(empresaId, operadoraId, codigoIntegracao);
        return ResponseEntity.ok(vinculaEmpresaOperadoraMapper.paraLista(vinculoEmpresaOperadoraList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VinculaEmpresaOperadoraDTO> atualizar (@PathVariable Long id, @RequestBody @Valid VinculaEmpresaOperadoraDTO vinculaEmpresaOperadoraDTO) {
        var vinculaEmpresaOperadora = this.vinculaEmpresaOperadoraService.atualizar(id, vinculaEmpresaOperadoraDTO);
        return ResponseEntity.ok(vinculaEmpresaOperadoraMapper.paraDTO(vinculaEmpresaOperadora));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VinculaEmpresaOperadoraDTO> buscarPorId (@PathVariable Long id) {
        var vinculaEmpresaOperadora = this.vinculaEmpresaOperadoraService.pesquisarPorId(id);
        return ResponseEntity.ok(vinculaEmpresaOperadoraMapper.paraDTO(vinculaEmpresaOperadora));
    }

    @DeleteMapping("/{id}")
    public void deletar (@PathVariable Long id) {
        this.vinculaEmpresaOperadoraService.deletar(id);
    }
}
