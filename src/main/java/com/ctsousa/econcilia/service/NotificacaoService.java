package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Notificacao;
import com.ctsousa.econcilia.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificacaoService {

    Notificacao salvar(final Notificacao notificacao);

    Notificacao buscarPorId(Long id);

    List<Notificacao> pesquisarLidas(Usuario usuario);

    List<Notificacao> pesquisarResolvidas(Usuario usuario);

    void marcarComoLida(Long id);

    void marcarComoResolvida(Long id);
}
