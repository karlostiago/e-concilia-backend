package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.PermissaoMapper;
import com.ctsousa.econcilia.model.dto.PermissaoDTO;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.PermissaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/permissoes")
public class PermissaoResource {

    private final PermissaoService permissaoService;

    private final PermissaoMapper permissaoMapper;

    public PermissaoResource(PermissaoService permissaoService, PermissaoMapper permissaoMapper) {
        this.permissaoService = permissaoService;
        this.permissaoMapper = permissaoMapper;
    }

    @PostMapping
    @PreAuthorize(Autorizar.CADASTRAR_PERMISSAO)
    public ResponseEntity<PermissaoDTO> cadastrar(@RequestBody @Valid PermissaoDTO permissaoDTO) {
        var permissao = permissaoService.salvar(permissaoMapper.paraEntidade(permissaoDTO));
        return ResponseEntity.ok(permissaoMapper.paraDTO(permissao));
    }
}
