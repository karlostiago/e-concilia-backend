package com.ctsousa.econcilia.processador.ifood;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.processador.Processador;
import com.ctsousa.econcilia.processador.ifood.dto.VendaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessadorIfood extends Processador<VendaDTO> {

    @Override
    public VendaDTO processar(List<Venda> vendas) {
        System.out.println("Processando vendas ifood");
        return null;
    }
}
