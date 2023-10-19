package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.IntegracaoMapper;
import com.ctsousa.econcilia.model.dto.IntegracaoDTO;
import com.ctsousa.econcilia.service.IntegracaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/integracoes")
public class IntegracaoResource {

    private final IntegracaoService integracaoService;

    private final IntegracaoMapper integracaoMapper;

    public IntegracaoResource(IntegracaoService integracaoService, IntegracaoMapper integracaoMapper) {
        this.integracaoService = integracaoService;
        this.integracaoMapper = integracaoMapper;
    }

    @PostMapping
    public ResponseEntity<IntegracaoDTO> cadastrar (@RequestBody @Valid IntegracaoDTO integracaoDTO) {
        var integracao = integracaoService.salvar(integracaoMapper.paraEntidade(integracaoDTO));
        return ResponseEntity.ok(integracaoMapper.paraDTO(integracao));
    }

    @GetMapping
    public ResponseEntity<List<IntegracaoDTO>> listar (@RequestParam(required = false) Long empresaId, @RequestParam(required = false) Long operadoraId, @RequestParam(required = false) String codigoIntegracao) {
        var integracoes = integracaoService.pesquisar(empresaId, operadoraId, codigoIntegracao);
        return ResponseEntity.ok(integracaoMapper.paraLista(integracoes));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntegracaoDTO> atualizar (@PathVariable Long id, @RequestBody @Valid IntegracaoDTO integracaoDTO) {
        var integracao = this.integracaoService.atualizar(id, integracaoDTO);
        return ResponseEntity.ok(integracaoMapper.paraDTO(integracao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntegracaoDTO> buscarPorId (@PathVariable Long id) {
        var integracao = this.integracaoService.pesquisarPorId(id);
        return ResponseEntity.ok(integracaoMapper.paraDTO(integracao));
    }

    @DeleteMapping("/{id}")
    public void deletar (@PathVariable Long id) {
        this.integracaoService.deletar(id);
    }
}
