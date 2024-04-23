package com.ctsousa.econcilia.service.impl;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Integracao;
import com.ctsousa.econcilia.model.IntegracaoBuffer;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.repository.EmpresaRepository;
import com.ctsousa.econcilia.repository.IntegracaoBufferRepository;
import com.ctsousa.econcilia.repository.IntegracaoRepository;
import com.ctsousa.econcilia.service.IntegracaoBufferService;
import com.ctsousa.econcilia.service.OperadoraService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntegracaoBufferServiceImpl implements IntegracaoBufferService {

    private final IntegracaoBufferRepository integracaoBufferRepository;

    private final IntegracaoRepository integracaoRepository;

    private final EmpresaRepository empresaRepository;

    private final OperadoraService operadoraService;

    public IntegracaoBufferServiceImpl(IntegracaoBufferRepository integracaoBufferRepository, IntegracaoRepository integracaoRepository, EmpresaRepository empresaRepository, OperadoraService operadoraService) {
        this.integracaoBufferRepository = integracaoBufferRepository;
        this.integracaoRepository = integracaoRepository;
        this.empresaRepository = empresaRepository;
        this.operadoraService = operadoraService;
    }

    @Override
    public void salvar(IntegracaoBuffer integracaoBuffer) {
        Empresa empresa = empresaRepository.porCnpj(integracaoBuffer.getCnpj());
        Operadora operadora = operadoraService.buscarPorDescricao(integracaoBuffer.getNomeOperadora());

        List<Integracao> integracoes = null;

        if (empresa != null) {
            integracoes = integracaoRepository.findByEmpresaAndOperadora(empresa, operadora);
        }

        if (integracoes == null || integracoes.isEmpty()) {
            integracaoBufferRepository.save(integracaoBuffer);
        }
    }
}
