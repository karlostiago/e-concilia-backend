package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Cancelamento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CancelamentoService {

    List<Cancelamento> buscar(String lojaId, String periodoId);
}
