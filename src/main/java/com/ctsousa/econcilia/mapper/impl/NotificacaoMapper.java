package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.Notificacao;
import com.ctsousa.econcilia.model.dto.NotificacaoDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExcludedCoverage
public class NotificacaoMapper implements EntidadeMapper<Notificacao, NotificacaoDTO>, DtoMapper<Notificacao, NotificacaoDTO>, ColecaoMapper<Notificacao, NotificacaoDTO> {

    private final EmpresaMapper empresaMapper;

    public NotificacaoMapper(EmpresaMapper empresaMapper) {
        this.empresaMapper = empresaMapper;
    }

    @Override
    public List<NotificacaoDTO> paraLista(List<Notificacao> notificacoes) {
        return notificacoes.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public NotificacaoDTO paraDTO(Notificacao notificacao) {
        NotificacaoDTO notificacaoDTO = new NotificacaoDTO();
        notificacaoDTO.setMensagem(notificacao.getMensagem());
        notificacaoDTO.setEmpresaDTO(empresaMapper.paraDTO(notificacao.getEmpresa()));
        notificacaoDTO.setResolvida(notificacao.getResolvida());
        notificacaoDTO.setLida(notificacao.getLida());
        return notificacaoDTO;
    }

    @Override
    public Notificacao paraEntidade(NotificacaoDTO notificacaoDTO) {
        Notificacao notificacao = new Notificacao();
        notificacao.setMensagem(notificacaoDTO.getMensagem());
        notificacao.setEmpresa(empresaMapper.paraEntidade(notificacaoDTO.getEmpresaDTO()));
        notificacao.setResolvida(notificacaoDTO.getResolvida());
        notificacao.setLida(notificacaoDTO.getLida());
        return notificacao;
    }
}
