package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.enumaration.TipoNotificacao;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Notificacao;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.repository.EmpresaRepository;
import com.ctsousa.econcilia.repository.NotificacaoRepository;
import com.ctsousa.econcilia.service.NotificacaoService;
import com.ctsousa.econcilia.service.UsuarioService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ctsousa.econcilia.util.StringUtil.temValor;

@Component
public class NotificacaoServiceImpl implements NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    private final UsuarioService usuarioService;

    private final EmpresaRepository empresaRepository;

    public NotificacaoServiceImpl(NotificacaoRepository notificacaoRepository, UsuarioService usuarioService, EmpresaRepository empresaRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.usuarioService = usuarioService;
        this.empresaRepository = empresaRepository;
    }

    @Override
    public List<Notificacao> pesquisar(Usuario usuario) {
        List<Empresa> empresas = getEmpresas(usuario.getId());
        List<Notificacao> notificacoesNaoLidas = pesquisarNaoLidas(empresas);
        List<Notificacao> notificacoesNaoResolvidas = pesquisarNaoResolvidas(empresas);
        return normalizarNotificacoes(notificacoesNaoLidas, notificacoesNaoResolvidas);
    }

    @Override
    public void marcarComoLida(Long id) {
        Notificacao notificacao = buscarPorId(id);

        if (Boolean.TRUE.equals(notificacao.getLida())) {
            throw new NotificacaoException("Notifcação já esta marcada como lida.");
        }

        notificacao.setLida(Boolean.TRUE);
        salvar(notificacao);
    }

    @Override
    public void marcarComoResolvida(Long id) {
        Notificacao notificacao = buscarPorId(id);

        if (TipoNotificacao.GLOBAL.equals(TipoNotificacao.porCodigo(notificacao.getTipoNotificacao()))) {
            throw new NotificacaoException("Não é permitido resolver notificação global.");
        }

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

    private List<Notificacao> normalizarNotificacoes(List<Notificacao> notificacoesLidas, List<Notificacao> notificacoesResolvidas) {
        Map<Long, Notificacao> notificacaoMap = Stream.concat(notificacoesLidas.stream(), notificacoesResolvidas.stream())
                .collect(Collectors.toMap(Notificacao::getId, notificacao -> notificacao, (existing, replacement) -> existing));


        return new ArrayList<>(notificacaoMap.values());
    }

    private List<Notificacao> pesquisarNaoLidas(List<Empresa> empresas) {
        return notificacaoRepository.naoLidas(empresas);
    }

    private List<Notificacao> pesquisarNaoResolvidas(List<Empresa> empresas) {
        return notificacaoRepository.naoResolvidas(empresas);
    }

    private List<Empresa> getEmpresas(final Long usuarioId) {
        Usuario usuario;

        try {
            usuario = usuarioService.pesquisarPorId(usuarioId);
        } catch (NotificacaoException e) {
            return empresaRepository.findAll();
        }

        List<Empresa> empresas = new ArrayList<>();

        String [] lojas = usuario.getLojasPermitidas().split(",");

        for (String loja : lojas) {
            empresas.add(new Empresa(Long.valueOf(loja)));
        }
        return empresas;
    }
}
