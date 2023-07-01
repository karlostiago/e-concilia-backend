package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.assembler.impl.EmpresaMapper;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.service.EmpresaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaResource {
    private final EmpresaService empresaService;
    private final EmpresaMapper empresaMapper;
    EmpresaResource(EmpresaService empresaService, EmpresaMapper empresaAssembler) {
        this.empresaService = empresaService;
        this.empresaMapper = empresaAssembler;
    }
    @PostMapping
    public ResponseEntity<EmpresaDTO> cadastrar (@RequestBody @Valid EmpresaDTO empresaDTO) {
        var empresa = this.empresaService.salvar(empresaMapper.paraEntidade(empresaDTO));
        return ResponseEntity.ok(empresaMapper.paraDTO(empresa));
    }
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listar (@RequestParam( required = false ) String razaoSocial, @RequestParam (required = false) String cnpj) {
        var empresas = this.empresaService.pesquisar(razaoSocial, cnpj);
        return ResponseEntity.ok(empresaMapper.paraLista(empresas));
    }
    @DeleteMapping("/{id}")
    public void deletar (@PathVariable Long id) {
        this.empresaService.deletar(id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> buscarPorId (@PathVariable Long id) {
        var empresa = this.empresaService.pesquisarPor(id);
        return ResponseEntity.ok(empresaMapper.paraDTO(empresa));
    }
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> atualizar (@PathVariable Long id, @RequestBody @Valid EmpresaDTO empresaDTO) {
        var empresa = this.empresaService.atualizar(id, empresaDTO);
        return ResponseEntity.ok(empresaMapper.paraDTO(empresa));
    }
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<EmpresaDTO> ativar (@PathVariable Long id) {
        return ResponseEntity.ok(empresaMapper.paraDTO(empresaService.ativar(id)));
    }
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<EmpresaDTO> desativar (@PathVariable Long id) {
        return ResponseEntity.ok(empresaMapper.paraDTO(empresaService.desativar(id)));
    }
}
