package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.enumaration.ImportacaoSituacao;
import com.ctsousa.econcilia.mapper.impl.ImportacaoMapper;
import com.ctsousa.econcilia.model.dto.ImportacaoDTO;
import com.ctsousa.econcilia.service.ImportacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/importacoes")
public class ImportacaoResource {

    private final ImportacaoService importacaoService;

    private final ImportacaoMapper importacaoMapper;

    public ImportacaoResource(ImportacaoService importacaoService, ImportacaoMapper importacaoMapper) {
        this.importacaoService = importacaoService;
        this.importacaoMapper = importacaoMapper;
    }

    @PostMapping
    public ResponseEntity<ImportacaoDTO> agendar (@RequestBody @Valid ImportacaoDTO importacaoDTO) {
        importacaoDTO.setSituacao(ImportacaoSituacao.AGENDADO);
        var importacao = importacaoService.agendar(importacaoMapper.paraEntidade(importacaoDTO));
        return ResponseEntity.ok(importacaoMapper.paraDTO(importacao));
    }

    @GetMapping
    public ResponseEntity<List<ImportacaoDTO>> importacoesAgendadas() {
        var importacoes = importacaoMapper.paraLista(importacaoService.buscarPorSituacaoAgendada());

        if (importacoes == null || importacoes.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(importacoes);
    }
}
