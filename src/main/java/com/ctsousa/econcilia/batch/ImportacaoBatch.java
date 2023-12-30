package com.ctsousa.econcilia.batch;

import com.ctsousa.econcilia.service.ImportacaoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ImportacaoBatch implements ProcessoBatch {

    private static final long QUINZE_MINUTOS = 900000L;

    private final ImportacaoService importacaoService;

    public ImportacaoBatch(ImportacaoService importacaoService) {
        this.importacaoService = importacaoService;
    }

    @Override
    @Scheduled(fixedRate = QUINZE_MINUTOS)
    public void processar() {
        System.out.println("processando....");
    }
}
