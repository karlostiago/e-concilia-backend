package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.ConciliadorDTO;
import com.ctsousa.econcilia.service.impl.ConciliadorIfoodServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

class ConciliadorIfoodServiceTest extends ApplicationIntegrationTest {

    private ConciliadorIfoodService conciliadorIfoodService;

    @Mock
    private TaxaService taxaService;

    @Mock
    private IntegracaoService integracaoService;

    @BeforeEach
    void setup() {
        conciliadorIfoodService = new ConciliadorIfoodServiceImpl(taxaService, integracaoService);
    }

    @Test
    void deveConciliar() {
        String codigoLoja = "1";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now().plusDays(30);

        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setRazaoSocial("EMPRESA IFOOD MOCK");

        Operadora operadora = new Operadora();
        operadora.setId(1L);
        operadora.setDescricao("ifood_mock");

        Taxa taxa = new Taxa();
        taxa.setValor(BigDecimal.valueOf(10D));

        Integracao integracao = new Integracao();
        integracao.setOperadora(operadora);
        integracao.setEmpresa(empresa);

        var valorTaxaPagamento = BigDecimal.valueOf(5D);
        var valorTaxaComissa = BigDecimal.valueOf(3D);

        Mockito.when(taxaService.buscarPor(empresa, operadora, "PAGAMENTO", valorTaxaPagamento.multiply(BigDecimal.valueOf(100))))
                        .thenReturn(taxa);

        Mockito.when(taxaService.buscarPor(empresa, operadora, "COMISS", valorTaxaComissa.multiply(BigDecimal.valueOf(100))))
                .thenReturn(taxa);

        Mockito.when(integracaoService.pesquisarPorCodigoIntegracao(codigoLoja))
                .thenReturn(integracao);

        ConciliadorDTO conciliadorDTO = conciliadorIfoodService.conciliar(codigoLoja, null, null, null, dtInicial, dtFinal);

        Assertions.assertFalse(conciliadorDTO.getVendas().isEmpty());
        Assertions.assertTrue(conciliadorDTO.getTotalizador().getTotalValorBruto().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(conciliadorDTO.getTotalizador().getTotalValorCancelado().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(conciliadorDTO.getTotalizador().getTotalValorPedido().compareTo(BigDecimal.ZERO) > 0);
        Assertions.assertTrue(conciliadorDTO.getTotalizador().getTotalValorLiquido().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoInformarIntegracao() {
        String codigoLoja = "1";
        LocalDate dtInicial = LocalDate.now();
        LocalDate dtFinal = LocalDate.now().plusDays(30);

        Operadora operadora = new Operadora();
        operadora.setDescricao("ifood_mock");

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> conciliadorIfoodService.conciliar(codigoLoja, null, null, null, dtInicial, dtFinal));

        Assertions.assertEquals("Não foi encontrada nenhuma empresa para o código integração.::: ", thrown.getMessage());
    }
}
