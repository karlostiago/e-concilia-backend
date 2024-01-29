package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.dto.OperadoraDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OperadoraService {

    Operadora salvar(Operadora operadora);

    Operadora buscarPorID(Long id);

    List<Operadora> pesquisar(String descricao);

    Operadora atualizar(Long id, OperadoraDTO operadoraDTO);

    void deletar(Long id);
}
