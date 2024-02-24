package com.ctsousa.econcilia.filter;

import com.ctsousa.econcilia.ApplicationUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VendaFilterTest extends ApplicationUnitTest {

    @Test
    void deveFiltrarPorBandeira() {
        VendaFilter filter = new VendaFilter(getVendas(), "VISA", null, null)
                .porBandeira();

        Assertions.assertEquals(5, filter.getVendasFiltradas().size());
    }

    @Test
    void deveFiltrarPorMetodoPagamento() {
        VendaFilter filter = new VendaFilter(getVendas(), null, "DÉBITO", null)
                .porMetodoPagamento();

        Assertions.assertEquals(1, filter.getVendasFiltradas().size());
    }

    @Test
    void deveFiltrarPorMetodoPagamentoBandeira() {
        VendaFilter filter = new VendaFilter(getVendas(), "VISA", "DÉBITO", null)
                .porMetodoPagamentoBandeira();

        Assertions.assertEquals(1, filter.getVendasFiltradas().size());

        filter = new VendaFilter(getVendas(), null, null, null)
                .porMetodoPagamentoBandeira();

        Assertions.assertEquals(5, filter.getVendasFiltradas().size());
    }

    @Test
    void deveFiltrarPorTipoRecebimento() {
        VendaFilter filter = new VendaFilter(getVendas(), null, null, "loja")
                .porMetodoPagamentoBandeira()
                .porTipoRecebimento();

        Assertions.assertTrue(filter.getVendasFiltradas().isEmpty());

        filter = new VendaFilter(getVendas(), null, null, "ifood")
                .porMetodoPagamentoBandeira()
                .porTipoRecebimento();

        Assertions.assertEquals(5, filter.getVendasFiltradas().size());
    }
}
