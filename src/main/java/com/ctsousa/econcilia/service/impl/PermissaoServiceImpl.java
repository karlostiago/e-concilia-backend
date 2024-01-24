package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Permissao;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.model.dto.PermissaoDTO;
import com.ctsousa.econcilia.repository.EmpresaRepository;
import com.ctsousa.econcilia.repository.PermissaoRepository;
import com.ctsousa.econcilia.service.EmpresaService;
import com.ctsousa.econcilia.service.PermissaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.ctsousa.econcilia.util.StringUtil.somenteNumero;

@Component
public class PermissaoServiceImpl implements PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public PermissaoServiceImpl(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @Override
    public Permissao salvar(Permissao permissao) {
        if (permissao.getUsuario() == null) {
            throw new NotificacaoException("Usuário não foi informado.");
        }
        if (permissao.getFuncionalidades() == null) {
            throw new NotificacaoException("Selecione ao menos uma permissão");
        }

        return permissaoRepository.save(permissao);
    }

    @Override
    public List<Permissao> pesquisar(Usuario usuario) {
        return null;
    }

    @Override
    public void deletar(Long id) {

    }

    @Override
    public Permissao pesquisarPorId(Long id) {
        return null;
    }

    @Override
    public Permissao atualizar(Long id, PermissaoDTO permissaoDTO) {
        return null;
    }
}