package com.ctsousa.econcilia.resource;


import com.ctsousa.econcilia.mapper.impl.UsuarioMapper;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.SegurancaDTO;
import com.ctsousa.econcilia.model.dto.UsuarioDTO;
import com.ctsousa.econcilia.service.SegurancaService;
import com.ctsousa.econcilia.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seguranca")
public class SegurancaResource {

    private final SegurancaService segurancaService;

    private final UsuarioMapper usuarioMapper;

    public SegurancaResource(SegurancaService segurancaService, UsuarioMapper usuarioMapper) {
        this.segurancaService = segurancaService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping("/login")
    public ResponseEntity<SegurancaDTO> login() {
        Usuario usuario = segurancaService.usuarioLogado();
        List<String> permissoes = segurancaService.permissoes();
        SegurancaDTO segurancaDTO = new SegurancaDTO();
        segurancaDTO.setUsuario(usuario != null ? usuarioMapper.paraDTO(usuario) : null);
        segurancaDTO.setPermissoes(permissoes);

        return ResponseEntity.ok(segurancaDTO);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}
