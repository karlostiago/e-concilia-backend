package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.MensagemMapper;
import com.ctsousa.econcilia.model.Usuario;
import com.ctsousa.econcilia.model.dto.MensagemDTO;
import com.ctsousa.econcilia.model.dto.NotificacaoDTO;
import com.ctsousa.econcilia.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoResource {

    private final NotificacaoService notificacaoService;

    private final MensagemMapper mensagemMapper;

    public NotificacaoResource(NotificacaoService notificacaoService, MensagemMapper mensagemMapper) {
        this.notificacaoService = notificacaoService;
        this.mensagemMapper = mensagemMapper;
    }

    @GetMapping
    public ResponseEntity<NotificacaoDTO> notificacoesNaoLidas(@RequestParam(required = false) Long usuarioId) {
        List<MensagemDTO> mensagens = mensagemMapper.paraLista(notificacaoService.pesquisar(new Usuario(usuarioId)));
        return ResponseEntity.ok(mensagemMapper.paraNotificacaoDTO(mensagens));
    }

    @PutMapping(value = "/marcar-como-lida/{id}")
    public ResponseEntity<Void> marcaComoLida(@PathVariable Long id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/marcar-como-resolvida/{id}")
    public ResponseEntity<Void> marcarComoResolvida(@PathVariable Long id) {
        notificacaoService.marcarComoResolvida(id);
        return ResponseEntity.ok().build();
    }
}
