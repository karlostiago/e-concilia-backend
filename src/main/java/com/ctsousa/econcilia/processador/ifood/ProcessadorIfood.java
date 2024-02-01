package com.ctsousa.econcilia.processador.ifood;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.model.dto.ConciliadorDTO;
import com.ctsousa.econcilia.processador.Processador;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessadorIfood extends Processador<ConciliadorDTO> {

    @Override
    public ConciliadorDTO processar(List<Venda> vendas) {
        return null;
    }
}
