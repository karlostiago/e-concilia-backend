package com.ctsousa.econcilia.mapper.impl;

import com.ctsousa.econcilia.annotation.ExcludedCoverage;
import com.ctsousa.econcilia.mapper.ColecaoMapper;
import com.ctsousa.econcilia.mapper.DtoMapper;
import com.ctsousa.econcilia.mapper.EntidadeMapper;
import com.ctsousa.econcilia.model.Contato;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Endereco;
import com.ctsousa.econcilia.model.Estado;
import com.ctsousa.econcilia.model.dto.ContatoDTO;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.model.dto.EnderecoDTO;
import com.ctsousa.econcilia.model.dto.EstadoDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExcludedCoverage
public class EmpresaMapper implements EntidadeMapper<Empresa, EmpresaDTO>, DtoMapper<Empresa, EmpresaDTO>, ColecaoMapper<Empresa, EmpresaDTO> {

    @Override
    public Empresa paraEntidade(EmpresaDTO empresaDTO) {
        Empresa empresa = new Empresa();
        empresa.setId(empresaDTO.getId());
        empresa.setCnpj(empresaDTO.getCnpj());
        empresa.setRazaoSocial(empresaDTO.getRazaoSocial());
        empresa.setNomeFantasia(empresaDTO.getNomeFantasia());
        empresa.setAtivo(empresaDTO.isAtivo());

        Contato contato = new Contato();
        contato.setCelular(empresaDTO.getContato().getCelular());
        contato.setEmail(empresaDTO.getContato().getEmail());
        contato.setTelefone(empresaDTO.getContato().getTelefone());
        empresa.setContato(contato);

        Endereco endereco = getEndereco(empresaDTO);

        Estado estado = new Estado();
        estado.setUf(empresaDTO.getEndereco().getEstado().getUf());
        endereco.setEstado(estado);
        empresa.setEndereco(endereco);

        return empresa;
    }

    @Override
    public EmpresaDTO paraDTO(Empresa empresa) {
        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setId(empresa.getId());
        empresaDTO.setCnpj(empresa.getCnpj());
        empresaDTO.setRazaoSocial(empresa.getRazaoSocial());
        empresaDTO.setNomeFantasia(empresa.getNomeFantasia());
        empresaDTO.setAtivo(empresa.isAtivo());

        ContatoDTO contatoDTO = new ContatoDTO();
        contatoDTO.setCelular(empresa.getContato().getCelular());
        contatoDTO.setEmail(empresa.getContato().getEmail());
        contatoDTO.setTelefone(empresa.getContato().getTelefone());
        empresaDTO.setContato(contatoDTO);

        EnderecoDTO enderecoDTO = getEnderecoDTO(empresa);

        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setUf(empresa.getEndereco().getEstado().getUf());
        enderecoDTO.setEstado(estadoDTO);
        empresaDTO.setEndereco(enderecoDTO);

        return empresaDTO;
    }

    @Override
    public List<EmpresaDTO> paraLista(List<Empresa> empresas) {
        return empresas.stream()
                .map(this::paraDTO)
                .toList();
    }

    private Endereco getEndereco(EmpresaDTO empresaDTO) {
        Endereco endereco = new Endereco();
        endereco.setCep(empresaDTO.getEndereco().getCep());
        endereco.setComplemento(empresaDTO.getEndereco().getComplemento());
        endereco.setNumero(empresaDTO.getEndereco().getNumero());
        endereco.setLogradouro(empresaDTO.getEndereco().getLogradouro());
        endereco.setCidade(empresaDTO.getEndereco().getCidade());
        endereco.setBairro(empresaDTO.getEndereco().getBairro());
        return endereco;
    }

    private EnderecoDTO getEnderecoDTO(Empresa empresa) {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep(empresa.getEndereco().getCep());
        enderecoDTO.setComplemento(empresa.getEndereco().getComplemento());
        enderecoDTO.setNumero(empresa.getEndereco().getNumero());
        enderecoDTO.setLogradouro(empresa.getEndereco().getLogradouro());
        enderecoDTO.setCidade(empresa.getEndereco().getCidade());
        enderecoDTO.setBairro(empresa.getEndereco().getBairro());
        return enderecoDTO;
    }
}
