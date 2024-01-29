package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.enumaration.TipoFuncionalidade;
import com.ctsousa.econcilia.mapper.impl.PermissaoMapper;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.PermissaoDTO;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.PermissaoService;
import com.ctsousa.econcilia.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/permissoes")
public class PermissaoResource {

    private final PermissaoService permissaoService;

    private final UsuarioService usuarioService;

    private final PermissaoMapper permissaoMapper;

    public PermissaoResource(PermissaoService permissaoService, PermissaoMapper permissaoMapper, UsuarioService usuarioService) {
        this.permissaoService = permissaoService;
        this.permissaoMapper = permissaoMapper;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @PreAuthorize(Autorizar.CADASTRAR_PERMISSAO)
    public ResponseEntity<PermissaoDTO> cadastrar(@RequestBody @Valid PermissaoDTO permissaoDTO) {
        var permissao = permissaoService.salvar(permissaoMapper.paraEntidade(permissaoDTO));
        return ResponseEntity.ok(permissaoMapper.paraDTO(permissao));
    }

    @GetMapping("/{id}/usuario")
    @PreAuthorize(Autorizar.PESQUISAR_PERMISSAO)
    public ResponseEntity<PermissaoDTO> buscarPorUsuario (@PathVariable Long id) {
        var usuario = usuarioService.pesquisarPorId(id);
        var permissao = permissaoService.pesquisar(usuario);
        return ResponseEntity.ok(permissaoMapper.paraDTO(permissao));
    }

    @PutMapping("/{id}")
    @PreAuthorize(Autorizar.EDITAR_PERMISSAO)
    public ResponseEntity<PermissaoDTO> atualizar (@PathVariable Long id, @RequestBody @Valid PermissaoDTO permissaoDTO) {
        var usuario = this.usuarioService.pesquisarPorId(permissaoDTO.getUsuario().getId());
        var permissao = this.permissaoService.atualizar(id, permissaoMapper.paraEntidade(permissaoDTO));

        permissao.setUsuario(usuario);
        return ResponseEntity.ok(permissaoMapper.paraDTO(permissao));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Autorizar.DELETAR_PERMISSAO)
    public void deletar (@PathVariable Long id) {
        this.permissaoService.deletar(id);
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_PERMISSAO)
    public ResponseEntity<List<PermissaoDTO>> listar (@RequestParam( required = false ) String nomeCompleto, @RequestParam (required = false) String tipoPermissao) {
        var permissoes = this.permissaoService.pesquisar(new Usuario(nomeCompleto), tipoPermissao);
        return ResponseEntity.ok(permissaoMapper.paraLista(permissoes));
    }
}
