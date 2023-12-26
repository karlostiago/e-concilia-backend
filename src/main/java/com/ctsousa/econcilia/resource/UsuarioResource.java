package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.UsuarioMapper;
import com.ctsousa.econcilia.model.dto.UsuarioDTO;
import com.ctsousa.econcilia.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

    private final UsuarioService usuarioService;

    private final UsuarioMapper usuarioMapper;

    public UsuarioResource(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrar (@RequestBody @Valid UsuarioDTO usuarioDTO) {
        usuarioService.confirmaEmail(usuarioDTO.getEmail(), usuarioDTO.getConfirmaEmail());
        usuarioService.confirmaSenha(usuarioDTO.getSenha(), usuarioDTO.getConfirmaSenha());
        var contrato = usuarioService.salvar(usuarioMapper.paraEntidade(usuarioDTO));
        return ResponseEntity.ok(usuarioMapper.paraDTO(contrato));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar (@RequestParam( required = false ) String nomeCompleto, @RequestParam (required = false) String email) {
        var usuarios = this.usuarioService.pesquisar(nomeCompleto, email);
        return ResponseEntity.ok(usuarioMapper.paraLista(usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId (@PathVariable Long id) {
        var usuario = this.usuarioService.pesquisarPorId(id);
        return ResponseEntity.ok(usuarioMapper.paraDTO(usuario));
    }

    @DeleteMapping("/{id}")
    public void deletar (@PathVariable Long id) {
        this.usuarioService.deletar(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar (@PathVariable Long id, @RequestBody @Valid UsuarioDTO usuarioDTO) {
        var usuario = this.usuarioService.atualizar(id, usuarioDTO);
        return ResponseEntity.ok(usuarioMapper.paraDTO(usuario));
    }
}
