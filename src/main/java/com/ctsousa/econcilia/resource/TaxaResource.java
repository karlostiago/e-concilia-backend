package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.mapper.impl.TaxaMapper;
import com.ctsousa.econcilia.model.Taxa;
import com.ctsousa.econcilia.model.dto.TaxaDTO;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.TaxaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/taxas")
public class TaxaResource {

    private final TaxaService taxaService;

    private final TaxaMapper taxaMapper;

    public TaxaResource(TaxaService taxaService, TaxaMapper taxaMapper) {
        this.taxaService = taxaService;
        this.taxaMapper = taxaMapper;
    }

    @PostMapping("/validar")
    public ResponseEntity<TaxaDTO> validar(@RequestBody @Valid TaxaDTO taxaDTO) {
        this.taxaService.validar(taxaMapper.paraEntidade(taxaDTO));
        taxaDTO.setExpiraEm(this.taxaService.calcularTempoExpiracao(taxaDTO.getEntraEmVigor(), taxaDTO.getValidoAte()));
        return ResponseEntity.ok(taxaDTO);
    }

    @GetMapping("/{contratoId}/contrato")
    @PreAuthorize(Autorizar.PESQUISAR_TAXA)
    public ResponseEntity<List<TaxaDTO>> buscarPorContrato(@PathVariable Long contratoId) {
        List<Taxa> taxas = taxaService.buscarPorContrato(contratoId);
        return ResponseEntity.ok(taxaMapper.paraLista(taxas));
    }

    @GetMapping("/{operadoraId}/operadora")
    @PreAuthorize(Autorizar.PESQUISAR_TAXA)
    public ResponseEntity<List<TaxaDTO>> buscarPorOperadora(@PathVariable Long operadoraId) {
        List<Taxa> taxas = taxaService.buscarPorOperadora(operadoraId);
        List<TaxaDTO> taxasDTO = taxaMapper.paraLista(taxas);
        taxasDTO.forEach(taxaDTO -> taxaDTO.setExpiraEm(taxaService.calcularTempoExpiracao(LocalDate.now(), taxaDTO.getValidoAte())));

        return ResponseEntity.ok(taxasDTO);
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_TAXA)
    public ResponseEntity<List<TaxaDTO>> listar() {
        List<Taxa> taxas = taxaService.buscarTodos();
        List<TaxaDTO> taxasDTO = taxaMapper.paraLista(taxas);
        taxasDTO.forEach(taxaDTO -> taxaDTO.setExpiraEm(taxaService.calcularTempoExpiracao(LocalDate.now(), taxaDTO.getValidoAte())));

        return ResponseEntity.ok(taxasDTO);
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize(Autorizar.ATIVAR_TAXA)
    public ResponseEntity<TaxaDTO> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(taxaMapper.paraDTO(taxaService.ativar(id)));
    }

    @PatchMapping("/{id}/desativar")
    @PreAuthorize(Autorizar.ATIVAR_TAXA)
    public ResponseEntity<TaxaDTO> desativar(@PathVariable Long id) {
        return ResponseEntity.ok(taxaMapper.paraDTO(taxaService.desativar(id)));
    }
}
