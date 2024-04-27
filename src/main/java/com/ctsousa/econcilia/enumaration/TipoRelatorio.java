package com.ctsousa.econcilia.enumaration;

import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.RelatorioConsolidadoDTO;
import com.ctsousa.econcilia.model.dto.RelatorioDTO;
import com.ctsousa.econcilia.model.dto.RelatorioTaxaDTO;
import com.ctsousa.econcilia.model.dto.RelatorioVendaDTO;
import com.ctsousa.econcilia.repository.ConsolidadoRepository;
import com.ctsousa.econcilia.repository.TaxaRepository;
import com.ctsousa.econcilia.repository.VendaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.econcilia.util.DataUtil.paraPtBr;
import static com.ctsousa.econcilia.util.DecimalUtil.monetarioPtBr;

public enum TipoRelatorio {

    VENDA {
        @Override
        public RelatorioDTO gerarDados(JpaRepository<?, ?> repository, LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
            VendaRepository vendaRepository = (VendaRepository) repository;
            List<Object[]> vendas = vendaRepository.por(dataInicial, dataFinal, empresa.getId(), operadora.getId());

            if (vendas.isEmpty()) return null;

            List<RelatorioVendaDTO> documentos = new ArrayList<>();
            for (Object [] venda : vendas) {
                RelatorioVendaDTO documento = new RelatorioVendaDTO();
                documento.setDataPedido(paraPtBr(((Date) venda[0]).toLocalDate()));
                documento.setNumeroDocumento(String.valueOf(venda[1]));
                documento.setRazaoSocial(String.valueOf(venda[2]));
                documento.setFormaPagamento(String.valueOf(venda[3]));
                documento.setResponsavel(String.valueOf(venda[4]));
                documento.setValorBruto(monetarioPtBr((BigDecimal) venda[5]));
                documento.setValorParcial(monetarioPtBr((BigDecimal)venda[6]));
                documento.setValorCancelado(monetarioPtBr((BigDecimal)venda[7]));
                documento.setValorComissao(monetarioPtBr((BigDecimal)venda[8]));
                documento.setValorTaxaEntrega(monetarioPtBr((BigDecimal)venda[9]));
                documento.setValorTaxaServico(monetarioPtBr((BigDecimal)venda[10]));
                documento.setTaxaComissao(monetarioPtBr(((BigDecimal)venda[11])
                        .multiply(BigDecimal.valueOf(100D))));
                documento.setTaxaComissaoPagamento(monetarioPtBr(((BigDecimal)venda[12])
                        .multiply(BigDecimal.valueOf(100D))));

                RelatorioDTO.Info docInfo = new RelatorioDTO.Info();
                docInfo.setTitulo("Relatório de vendas para:");
                docInfo.setNome(empresa.getRazaoSocial());
                docInfo.setEndereco(empresa.getEndereco().getLogradouro() + ", " + empresa.getEndereco().getNumero() + ", " + empresa.getEndereco().getBairro());
                docInfo.setTelefone(empresa.getContato().getCelular());
                docInfo.setEmail(empresa.getContato().getEmail().toLowerCase());
                documento.setInfo(docInfo);

                documentos.add(documento);
            }

            return new RelatorioDTO(null, null, documentos);
        }
    },

    TAXA {
        @Override
        public RelatorioDTO gerarDados(JpaRepository<?, ?> repository, LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
            TaxaRepository taxaRepository = (TaxaRepository) repository;
            List<Taxa> taxas = taxaRepository.por(dataInicial, dataFinal, empresa.getId(), operadora.getId());

            if (taxas.isEmpty()) return null;

            List<RelatorioTaxaDTO> documentos = new ArrayList<>();

            for (Taxa taxa : taxas) {
                RelatorioTaxaDTO documento = new RelatorioTaxaDTO();
                documento.setDescricao(taxa.getDescricao());
                documento.setTipo(taxa.getTipo().name());
                documento.setValor(monetarioPtBr(taxa.getValor()));
                documento.setEntraEmVigor(paraPtBr(taxa.getEntraEmVigor()));
                documento.setValidoAte(paraPtBr(taxa.getValidoAte()));
                documento.setAtivo(Boolean.TRUE.equals(taxa.getAtivo()) ? "SIM" : "NÂO");

                RelatorioDTO.Info docInfo = new RelatorioDTO.Info();
                docInfo.setTitulo("Relatório de taxas para:");
                docInfo.setNome(empresa.getRazaoSocial());
                docInfo.setEndereco(empresa.getEndereco().getLogradouro() + ", " + empresa.getEndereco().getNumero() + ", " + empresa.getEndereco().getBairro());
                docInfo.setTelefone(empresa.getContato().getCelular());
                docInfo.setEmail(empresa.getContato().getEmail().toLowerCase());
                documento.setInfo(docInfo);

                documentos.add(documento);
            }

            return new RelatorioDTO(null, documentos, null);
        }
    },

    CONSOLIDACAO {
        @Override
        public RelatorioDTO gerarDados(JpaRepository<?, ?> repository, LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora) {
            ConsolidadoRepository consolidadoRepository = (ConsolidadoRepository) repository;
            List<Object[]> consolidadosGerados = consolidadoRepository.por(dataInicial, dataFinal, empresa.getId(), operadora.getId());

            if (consolidadosGerados.isEmpty()) return null;

            List<RelatorioConsolidadoDTO> documentos = new ArrayList<>();

            for (Object [] consolidado : consolidadosGerados) {
                RelatorioConsolidadoDTO documento = new RelatorioConsolidadoDTO();
                documento.setPeriodo(consolidado[0] == null ? "" : String.valueOf(consolidado[0]));
                documento.setQuantidadeVenda(monetarioPtBr((BigDecimal) consolidado[2]));
                documento.setTotalBruto(monetarioPtBr((BigDecimal) consolidado[3]));
                documento.setTicketMedio(monetarioPtBr((BigDecimal) consolidado[4]));
                documento.setValorAntecipado(monetarioPtBr((BigDecimal) consolidado[5]));
                documento.setTotalTaxaEntrega(monetarioPtBr((BigDecimal) consolidado[6]));
                documento.setTotalPromocao(monetarioPtBr((BigDecimal) consolidado[7]));
                documento.setTotalTransacaoPagamento(monetarioPtBr((BigDecimal) consolidado[8]));
                documento.setTotalComissao(monetarioPtBr((BigDecimal) consolidado[9]));
                documento.setTotalCancelado(monetarioPtBr((BigDecimal) consolidado[10]));
                documento.setTotalTaxaServico(monetarioPtBr((BigDecimal) consolidado[11]));
                documento.setTotalTaxaManutencao(monetarioPtBr((BigDecimal) consolidado[12]));
                documento.setTotalRepasse(monetarioPtBr((BigDecimal) consolidado[13]));

                RelatorioDTO.Info docInfo = new RelatorioDTO.Info();
                docInfo.setTitulo("Relatório de vendas consolidadas para:");
                docInfo.setNome(empresa.getRazaoSocial());
                docInfo.setEndereco(empresa.getEndereco().getLogradouro() + ", " + empresa.getEndereco().getNumero() + ", " + empresa.getEndereco().getBairro());
                docInfo.setTelefone(empresa.getContato().getCelular());
                docInfo.setEmail(empresa.getContato().getEmail().toLowerCase());
                documento.setInfo(docInfo);

                documentos.add(documento);
            }
            
            return new RelatorioDTO(documentos, null, null);
        }
    };

    public abstract RelatorioDTO gerarDados(JpaRepository<?, ?> repository, LocalDate dataInicial, LocalDate dataFinal, Empresa empresa, Operadora operadora);

    public static TipoRelatorio porDescricao(final String descricao) {
        for (TipoRelatorio tipo : TipoRelatorio.values()) {
            if (tipo.name().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        return null;
    }
}
