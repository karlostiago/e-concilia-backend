package com.ctsousa.econcilia.assembler;

import com.ctsousa.econcilia.model.Contato;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Endereco;
import com.ctsousa.econcilia.model.Estado;
import com.ctsousa.econcilia.model.dto.ContatoDTO;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.model.dto.EnderecoDTO;
import com.ctsousa.econcilia.model.dto.EstadoDTO;
import org.springframework.stereotype.Component;

@Component
public class EmpresaAssembler implements EntidadeAssembler<Empresa, EmpresaDTO>, DtoAssembler<Empresa, EmpresaDTO> {

    @Override
    public Empresa paraEntidade(EmpresaDTO empresaDTO) {
        Empresa empresa = new Empresa();
        empresa.setCnpj(empresaDTO.getCnpj());
        empresa.setRazaoSocial(empresaDTO.getRazaoSocial());
        empresa.setNomeFantasia(empresaDTO.getNomeFantasia());

        Contato contato = new Contato();
        contato.setCelular(empresaDTO.getContato().getCelular());
        contato.setEmail(empresaDTO.getContato().getEmail());
        contato.setTelefone(empresaDTO.getContato().getTelefone());
        empresa.setContato(contato);

        Endereco endereco = new Endereco();
        endereco.setCep(empresaDTO.getEndereco().getCep());
        endereco.setComplemento(empresaDTO.getEndereco().getComplemento());
        endereco.setNumero(empresaDTO.getEndereco().getNumero());
        endereco.setLogradouro(empresaDTO.getEndereco().getLogradouro());
        endereco.setCidade(empresaDTO.getEndereco().getCidade());

        Estado estado = new Estado();
        estado.setUf(empresaDTO.getEndereco().getEstado().getUf());
        endereco.setEstado(estado);
        empresa.setEndereco(endereco);

        return empresa;
    }

    @Override
    public EmpresaDTO paraDTO(Empresa empresa) {
        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setCnpj(empresa.getCnpj());
        empresaDTO.setRazaoSocial(empresa.getRazaoSocial());
        empresaDTO.setNomeFantasia(empresa.getNomeFantasia());

        ContatoDTO contatoDTO = new ContatoDTO();
        contatoDTO.setCelular(empresa.getContato().getCelular());
        contatoDTO.setEmail(empresa.getContato().getEmail());
        contatoDTO.setTelefone(empresa.getContato().getTelefone());
        empresaDTO.setContato(contatoDTO);

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep(empresa.getEndereco().getCep());
        enderecoDTO.setComplemento(empresa.getEndereco().getComplemento());
        enderecoDTO.setNumero(empresa.getEndereco().getNumero());
        enderecoDTO.setLogradouro(empresa.getEndereco().getLogradouro());
        enderecoDTO.setCidade(empresa.getEndereco().getCidade());

        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setUf(empresa.getEndereco().getEstado().getUf());
        enderecoDTO.setEstado(estadoDTO);
        empresaDTO.setEndereco(enderecoDTO);

        return empresaDTO;
    }
}
