package com.ctsousa.econcilia;

import com.ctsousa.econcilia.enumaration.Perfil;
import com.ctsousa.econcilia.enumaration.TipoValor;
import com.ctsousa.econcilia.model.*;
import com.ctsousa.econcilia.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

@ApplicationTest
public class ApplicationIntegrationTest extends ApplicationUnitTest {

    @Autowired
    protected TaxaRepository taxaRepository;

    @Autowired
    protected ContratoRepository contratoRepository;

    @Autowired
    protected OperadoraRepository operadoraRepository;

    @Autowired
    protected EmpresaRepository empresaRepository;

    @Autowired
    protected IntegracaoRepository integracaoRepository;

    @Autowired
    protected ImportacaoRepository importacaoRepository;

    @Autowired
    protected UsuarioRepository usuarioRepository;

    @Autowired
    protected PermissaoRepository permissaoRepository;

    @Autowired
    protected NotificacaoRepository notificacaoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @AfterEach
    void destroy() {
        deletarMassaDeDados();
    }

    protected void criarMassaDeDados() {
        deletarMassaDeDados();

        criarSalvarEmpresa();
        criarSalvarOperadora();
        criarSalvarContrato();
        criarSalvarTaxas();
    }

    protected void deletarMassaDeDados() {
        notificacaoRepository.deleteAll();
        permissaoRepository.deleteAll();
        usuarioRepository.deleteAll();
        vendaRepository.deleteAll();
        importacaoRepository.deleteAll();
        integracaoRepository.deleteAll();
        taxaRepository.deleteAll();
        contratoRepository.deleteAll();
        operadoraRepository.deleteAll();
        empresaRepository.deleteAll();
    }

    protected Usuario getUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("Usuario teste");
        usuario.setSenha("senha");
        usuario.setEmail("email@email.com");
        usuario.setPerfil(Perfil.ADMINISTRADOR);
        usuario.setLojasPermitidas("1, 2");
        return usuario;
    }

    protected Empresa getEmpresa() {
        Contato contato = new Contato();
        contato.setCelular("000000000");
        contato.setTelefone("0000000000");
        contato.setEmail("email@email.com");

        Estado estado = new Estado();
        estado.setUf("CE");

        Endereco endereco = new Endereco();
        endereco.setCep("000000");
        endereco.setCidade("Cidade teste");
        endereco.setBairro("Bairro teste");
        endereco.setEstado(estado);
        endereco.setNumero("123");
        endereco.setComplemento("Complemento teste");
        endereco.setLogradouro("Logradouro teste");

        Empresa empresa = new Empresa();
        empresa.setAtivo(Boolean.TRUE);
        empresa.setCnpj("00.000.000/0001-91");
        empresa.setNomeFantasia("Banco do brasil");
        empresa.setRazaoSocial("Banco do brasil");
        empresa.setContato(contato);
        empresa.setEndereco(endereco);
        return empresa;
    }

    protected Operadora getOperadora() {
        Operadora operadora = new Operadora();
        operadora.setDescricao("Operadora teste");
        operadora.setAtivo(Boolean.TRUE);
        return operadora;
    }

    protected Taxa getTaxa(Contrato contrato) {
        Taxa taxa = new Taxa();
        taxa.setTipo(TipoValor.MONETARIO);
        taxa.setValor(BigDecimal.TEN);
        taxa.setAtivo(Boolean.TRUE);
        taxa.setDescricao("Taxa teste 1");
        taxa.setEntraEmVigor(LocalDate.now());
        taxa.setValidoAte(LocalDate.now().plusDays(30));
        taxa.setContrato(contrato);
        return taxa;
    }

    protected Integracao getIntegracao(Empresa empresa, Operadora operadora) {
        Integracao integracao = new Integracao();
        integracao.setOperadora(operadora);
        integracao.setEmpresa(empresa);
        integracao.setCodigoIntegracao("123456");
        return integracao;
    }

    protected void criarSalvarEmpresa() {
        empresaRepository.save(getEmpresa());
    }

    protected void criarSalvarOperadora() {
        operadoraRepository.save(getOperadora());
    }

    protected void criarSalvarContrato() {
        Empresa empresa = empresaRepository.porCnpj("00000000000191");
        Operadora operadora = operadoraRepository.porDescricao("Operadora teste".toUpperCase()).get(0);

        Contrato contrato = new Contrato();
        contrato.setEmpresa(empresa);
        contrato.setOperadora(operadora);
        contrato.setAtivo(Boolean.TRUE);
        contratoRepository.save(contrato);
    }

    private void criarSalvarTaxas() {
        Empresa empresa = empresaRepository.porCnpj("00000000000191");
        Contrato contrato = contratoRepository.findByEmpresa(empresa).get(0);

        Taxa taxa1 = new Taxa();
        taxa1.setTipo(TipoValor.MONETARIO);
        taxa1.setValor(BigDecimal.TEN);
        taxa1.setAtivo(Boolean.TRUE);
        taxa1.setDescricao("Taxa teste 1");
        taxa1.setEntraEmVigor(LocalDate.now());
        taxa1.setValidoAte(LocalDate.now().plusDays(30));
        taxa1.setContrato(contrato);
        taxaRepository.save(taxa1);

        Taxa taxa2 = new Taxa();
        taxa2.setTipo(TipoValor.MONETARIO);
        taxa2.setValor(BigDecimal.ONE);
        taxa2.setAtivo(Boolean.TRUE);
        taxa2.setDescricao("Taxa teste 2");
        taxa2.setEntraEmVigor(LocalDate.now());
        taxa2.setValidoAte(LocalDate.now().plusDays(30));
        taxa2.setContrato(contrato);
        taxaRepository.save(taxa2);
    }
}
