package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Notificacao;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.repository.NotificacaoRepository;
import com.ctsousa.econcilia.service.NotificacaoService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.econcilia.util.StringUtil.temValor;

@Component
public class NotificacaoServiceImpl implements NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoServiceImpl(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    @Override
    public List<Notificacao> pesquisar(Usuario usuario) {
        List<Empresa> empresas = getEmpresas(usuario);
        List<Notificacao> notificacoesLidas = pesquisarNaoLidas(empresas);
        List<Notificacao> notificacoesResolvidas = pesquisarNaoResolvidas(empresas);
        List<Notificacao> notificacoes = new ArrayList<>(notificacoesLidas.size() + notificacoesResolvidas.size());
        notificacoes.addAll(notificacoesLidas);
        notificacoes.addAll(notificacoesResolvidas);
        return notificacoes;
    }

    @Override
    public void marcarComoLida(Long id) {
        Notificacao notificacao = buscarPorId(id);
        notificacao.setLida(Boolean.TRUE);
        salvar(notificacao);
    }

    @Override
    public void marcarComoResolvida(Long id) {
        Notificacao notificacao = buscarPorId(id);
        notificacao.setResolvida(Boolean.TRUE);
        notificacao.setLida(Boolean.TRUE);
        salvar(notificacao);
    }

    @Override
    public Notificacao salvar(Notificacao notificacao) {

        if (notificacao.getEmpresa() == null) {
            throw new NotificacaoException("Nenhuma empresa informada. Por gentileza informe uma empresa.");
        }

        if (Boolean.FALSE.equals(temValor(notificacao.getMensagem()))) {
            throw new NotificacaoException("Nenhuma mensagem informada. Por gentileza informe uma mensagem.");
        }

        return notificacaoRepository.save(notificacao);
    }

    @Override
    public Notificacao buscarPorId(final Long id) {
        return notificacaoRepository.findById(id)
                .orElseThrow(() -> new NotificacaoException(String.format("Nenhuma notificacao com id %s não encontrado.", id)));
    }

    private List<Notificacao> pesquisarNaoLidas(List<Empresa> empresas) {
        return notificacaoRepository.naoLidas(empresas);
    }

    private List<Notificacao> pesquisarNaoResolvidas(List<Empresa> empresas) {
        return notificacaoRepository.naoResolvidas(empresas);
    }

    private List<Empresa> getEmpresas(Usuario usuario) {
        List<Empresa> empresas = new ArrayList<>();

        String [] lojas = usuario.getLojasPermitidas().split(",");

        for (String loja : lojas) {
            empresas.add(new Empresa(Long.valueOf(loja)));
        }
        return empresas;
    }
}
