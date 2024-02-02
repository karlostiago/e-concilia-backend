package com.ctsousa.econcilia.processador.ifood;

import com.ctsousa.econcilia.model.Venda;
import com.ctsousa.econcilia.processador.Processador;
import com.ctsousa.econcilia.processador.ifood.dto.VendaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessadorUberEats extends Processador<VendaDTO> {

    @Override
    public VendaDTO processar(List<Venda> vendas) {
        throw new UnsupportedOperationException("Operação não suportada por esse processador.");
    }
}
