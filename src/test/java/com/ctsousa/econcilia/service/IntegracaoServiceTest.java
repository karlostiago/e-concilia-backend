package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.ApplicationIntegrationTest;
import com.ctsousa.econcilia.exceptions.NotificacaoException;
import com.ctsousa.econcilia.integration.ifood.IfoodGateway;
import com.ctsousa.econcilia.mapper.impl.IntegracaoMapper;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.IntegracaoDTO;
import com.ctsousa.econcilia.service.impl.IntegracaoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

class IntegracaoServiceTest extends ApplicationIntegrationTest {

    private IntegracaoService integracaoService;

    @Autowired
    private IntegracaoMapper mapper;

    @Mock
    private IfoodGateway ifoodGateway;

    @BeforeEach
    void setup() {
        integracaoService = new IntegracaoServiceImpl(integracaoRepository, mapper, ifoodGateway);
        criarMassaDeDados();
    }

    @Test
    void deveCadastrar() {
        Integracao integracao = criarIntegracao();

        Assertions.assertNotNull(integracao.getId());

        Mockito.verify(ifoodGateway, Mockito.times(1))
                .verifyMerchantById("123456");
    }

    @Test
    void deveDeletar() {
        Integracao integracao = criarIntegracao();

        integracaoService.deletar(integracao.getId());

        Assertions.assertEquals(0, integracaoRepository.findAll().size());
    }

    @Test
    void deveAtualizar() {
        Integracao integracao = criarIntegracao();

        Assertions.assertEquals("123456", integracao.getCodigoIntegracao());

        integracao.setCodigoIntegracao("789456");
        IntegracaoDTO integracaoDTO = mapper.paraDTO(integracao);

        integracao = integracaoService.atualizar(integracao.getId(), integracaoDTO);

        Assertions.assertEquals(integracaoDTO.getCodigoIntegracao(), integracao.getCodigoIntegracao());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoExistirIntegracaoComMesmoCodigo() {
        Integracao integracao = criarIntegracao();
        String codigoIntegracao = integracao.getCodigoIntegracao();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> integracaoService.salvar(integracao));

        Assertions.assertEquals("Já existe uma integração..:: " + codigoIntegracao + ", com a operadora..:: " + integracao.getOperadora().getDescricao(), thrown.getMessage());
    }

    @Test
    void devePesquisarPorCodigoIntegracao() {
        criarIntegracao();

        Assertions.assertNotNull(integracaoService.pesquisarPorCodigoIntegracao("123456"));
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoNaoExistirIntegracaoPeloCodigoInformado() {
        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> integracaoService.pesquisarPorCodigoIntegracao("1234567"));

        Assertions.assertEquals("Integração com código integração 1234567 não encontrado", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoEmpresaInexistente() {
        criarIntegracao();

        Empresa empresa = new Empresa(100L);
        Operadora operadora = new Operadora(99L);

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> integracaoService.pesquisar(empresa, operadora));

        Assertions.assertEquals("Não existe integração para a empresa com id ::: 100", thrown.getMessage());
    }

    @Test
    void deveRetornarNotificacaoExceptionQuandoTiverMaisDeUmaIntegracaoParaMesmaEmpresaOperadora() {
        Integracao integracao = criarIntegracao();

        integracao.setId(null);
        integracao.setCodigoIntegracao("789456");
        integracaoRepository.save(integracao);

        Assertions.assertEquals(2, integracaoRepository.findAll().size());

        Empresa empresa = integracao.getEmpresa();
        Operadora operadora = integracao.getOperadora();

        var thrown = Assertions.assertThrows(NotificacaoException.class, () -> integracaoService.pesquisar(empresa, operadora));

        Assertions.assertEquals("Existe mais de uma integração para a empresa com id ::: " + empresa.getId(), thrown.getMessage());
    }

    @Test
    void deveRetornarQuandoInformarEmpresaOperadora() {
        Integracao integracao = criarIntegracao();

        Empresa empresa = integracao.getEmpresa();
        Operadora operadora = integracao.getOperadora();

        Assertions.assertNotNull(integracaoService.pesquisar(empresa, operadora).getId());
    }

    @Test
    void devePesquisar() {
        Integracao integracao = criarIntegracao();

        Long empresaId = integracao.getEmpresa().getId();
        Long operadoraId = integracao.getOperadora().getId();
        String codigoIntegracao = "123456";

        Assertions.assertEquals(1,
                integracaoService.pesquisar(null, null, codigoIntegracao).size());

        Assertions.assertEquals(1,
                integracaoService.pesquisar(empresaId, operadoraId, null).size());

        Assertions.assertEquals(1,
                integracaoService.pesquisar(empresaId, null, null).size());

        Assertions.assertEquals(1,
                integracaoService.pesquisar(null, operadoraId, null).size());

        Assertions.assertEquals(1,
                integracaoService.pesquisar(null, null, null).size());
    }

    private Integracao criarIntegracao() {
        Empresa empresa = empresaRepository.porCnpj("00000000000191");
        Operadora operadora = operadoraRepository.porDescricao("OPERADORA TESTE")
                .get(0);

        Integracao integracao = new Integracao();
        integracao.setOperadora(operadora);
        integracao.setEmpresa(empresa);
        integracao.setCodigoIntegracao("123456");

        return integracaoService.salvar(integracao);
    }
}
