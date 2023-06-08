package com.ctsousa.econcilia.assembler;

import com.ctsousa.econcilia.integration.receitaws.json.DadosCnpjJson;
import com.ctsousa.econcilia.model.dto.ContatoDTO;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.model.dto.EnderecoDTO;
import com.ctsousa.econcilia.model.dto.EstadoDTO;
import org.springframework.stereotype.Component;

@Component
public class CnpjAssembler implements DtoAssembler<DadosCnpjJson, EmpresaDTO> {

    @Override
    public EmpresaDTO paraDTO(DadosCnpjJson dadosCnpj) {
        final EmpresaDTO empresaDTO = new EmpresaDTO();

        if (dadosCnpj != null) {
            empresaDTO.setCnpj(dadosCnpj.getCnpj());
            empresaDTO.setNomeFantasia(dadosCnpj.getNomeFantasia());
            empresaDTO.setRazaoSocial(dadosCnpj.getRazaoSocial());

            var endereco = new EnderecoDTO();
            endereco.setLogradouro(dadosCnpj.getLogradouro());
            endereco.setBairro(dadosCnpj.getBairro());
            endereco.setCidade(dadosCnpj.getCidade());
            endereco.setNumero(dadosCnpj.getNumero());
            endereco.setCep(dadosCnpj.getCep());

            var contato = new ContatoDTO();
            contato.setEmail(dadosCnpj.getEmail());
            contato.setTelefone(dadosCnpj.getTelefone());
            empresaDTO.setContato(contato);

            var estado = new EstadoDTO();
            estado.setUf(dadosCnpj.getUf());
            endereco.setEstado(estado);

            empresaDTO.setEndereco(endereco);
        }

        return empresaDTO;
    }
}
