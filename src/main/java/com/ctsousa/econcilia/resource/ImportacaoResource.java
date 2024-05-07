package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.mapper.impl.ImportacaoMapper;
import com.ctsousa.econcilia.model.dto.ImportacaoDTO;
import com.ctsousa.econcilia.scheduler.Scheduler;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.ImportacaoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/importacoes")
public class ImportacaoResource {

    private final ImportacaoService importacaoService;

    private final ImportacaoMapper importacaoMapper;

    private final Scheduler scheduler;

    public ImportacaoResource(ImportacaoService importacaoService, ImportacaoMapper importacaoMapper, @Qualifier("importacaoProgramadaScheduler") Scheduler scheduler) {
        this.importacaoService = importacaoService;
        this.importacaoMapper = importacaoMapper;
        this.scheduler = scheduler;
    }

    @PostMapping
    @PreAuthorize(Autorizar.AGENDAR_IMPORTACAO)
    public ResponseEntity<ImportacaoDTO> agendar(@RequestBody @Valid ImportacaoDTO importacaoDTO) {
        importacaoDTO.setSituacao(ImportacaoSituacao.AGENDADO.getDescricao().toUpperCase());
        var importacao = importacaoService.agendar(importacaoMapper.paraEntidade(importacaoDTO));
        return ResponseEntity.ok(importacaoMapper.paraDTO(importacao));
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_IMPORTACAO)
    public ResponseEntity<List<ImportacaoDTO>> importacoesAgendadas() {
        var importacoes = importacaoMapper.paraLista(importacaoService.buscarImportacoes());

        if (importacoes == null || importacoes.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(importacoes);
    }

    @PostMapping(value = "/executar-manual")
    @PreAuthorize(Autorizar.AGENDAR_IMPORTACAO)
    public ResponseEntity<Void> executarManual() {
        this.importacaoService.temImportacaoProgramada();
        scheduler.processar();

        return ResponseEntity.ok().build();
    }
}
