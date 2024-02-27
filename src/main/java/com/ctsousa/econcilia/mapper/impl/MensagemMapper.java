package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.model.Notificacao;
import com.ctsousa.econcilia.model.dto.MensagemDTO;
import com.ctsousa.econcilia.model.dto.NotificacaoDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExcludedCoverage
public class MensagemMapper implements DtoMapper<Notificacao, MensagemDTO>, ColecaoMapper<Notificacao, MensagemDTO> {

    private final EmpresaMapper empresaMapper;

    public MensagemMapper(EmpresaMapper empresaMapper) {
        this.empresaMapper = empresaMapper;
    }

    @Override
    public List<MensagemDTO> paraLista(List<Notificacao> notificacoes) {
        return notificacoes.stream()
                .map(this::paraDTO)
                .toList();
    }

    @Override
    public MensagemDTO paraDTO(Notificacao notificacao) {
        MensagemDTO mensagemDTO = new MensagemDTO();
        mensagemDTO.setId(notificacao.getId());
        mensagemDTO.setConteudo(notificacao.getMensagem());
        mensagemDTO.setEmpresaDTO(empresaMapper.paraDTO(notificacao.getEmpresa()));
        mensagemDTO.setResolvida(notificacao.getResolvida());
        mensagemDTO.setLida(notificacao.getLida());
        mensagemDTO.setTipo(notificacao.getTipoNotificacao());
        return mensagemDTO;
    }

    public NotificacaoDTO paraNotificacaoDTO(List<MensagemDTO> mensagens) {
        NotificacaoDTO notificacaoDTO = new NotificacaoDTO();
        notificacaoDTO.setMensagens(mensagens);
        return notificacaoDTO;
    }
}
