package com.ctsousa.econcilia.service;

import com.ctsousa.econcilia.model.Venda;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConciliadorIfoodService {

    List<Venda> conciliarTaxas(final List<Venda> vendas, final String lojaId);
}
