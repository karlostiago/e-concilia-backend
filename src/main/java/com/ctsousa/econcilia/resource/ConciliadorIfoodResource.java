package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.model.dto.ConciliadorDTO;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.ConciliadorIfoodService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/conciliadores/ifood", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConciliadorIfoodResource {

    private final ConciliadorIfoodService conciliadorIfoodService;

    public ConciliadorIfoodResource(ConciliadorIfoodService conciliadorIfoodService) {
        this.conciliadorIfoodService = conciliadorIfoodService;
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_CONCILIADOR_IFOOD)
    public ResponseEntity<ConciliadorDTO> vendas(@RequestParam(name = "lojaId") final String lojaId,
                                                 @RequestParam(name = "dtInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtInicial,
                                                 @RequestParam(name = "dtFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dtFinal,
                                                 @RequestParam(name = "metodoPagamento", required = false) final String metodoPagamento,
                                                 @RequestParam(name = "bandeira", required = false) final String bandeira,
                                                 @RequestParam(name = "tipoRecebimento", required = false) final String tipoRecebimento) {

        var conciliador = conciliadorIfoodService.conciliar(lojaId, metodoPagamento, bandeira, tipoRecebimento, dtInicial, dtFinal);
        return ResponseEntity.ok(conciliador);
    }
}
