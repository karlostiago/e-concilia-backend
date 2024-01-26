package com.ctsousa.econcilia.resource;


import com.ctsousa.econcilia.mapper.impl.UsuarioMapper;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.UsuarioDTO;
import com.ctsousa.econcilia.service.SegurancaService;
import com.ctsousa.econcilia.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UsuarioDTO> login() {
        Usuario usuario = segurancaService.usuarioLogado();

        if (usuario == null) {
            return null;
        }

        return ResponseEntity.ok(usuarioMapper.paraDTO(usuario));
    }
}
