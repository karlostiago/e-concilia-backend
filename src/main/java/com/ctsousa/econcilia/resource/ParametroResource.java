package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.enumaration.TipoParametro;
import com.ctsousa.econcilia.mapper.impl.ParametroMapper;
import com.ctsousa.econcilia.model.Empresa;
import com.ctsousa.econcilia.model.Operadora;
import com.ctsousa.econcilia.model.Parametro;
import com.ctsousa.econcilia.model.dto.ParametroDTO;
import com.ctsousa.econcilia.security.Autorizar;
import com.ctsousa.econcilia.service.EmpresaService;
import com.ctsousa.econcilia.service.OperadoraService;
import com.ctsousa.econcilia.service.ParametroService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/parametros")
public class ParametroResource {

    private final ParametroMapper mapper;

    private final EmpresaService empresaService;

    private final OperadoraService operadoraService;

    private final ParametroService parametroService;

    private Empresa empresa;

    private Operadora operadora;

    public ParametroResource(ParametroMapper mapper, EmpresaService empresaService, OperadoraService operadoraService, ParametroService parametroService) {
        this.mapper = mapper;
        this.empresaService = empresaService;
        this.operadoraService = operadoraService;
        this.parametroService = parametroService;
    }

    @GetMapping
    @PreAuthorize(Autorizar.PESQUISAR_PARAMETRO)
    public ResponseEntity<List<ParametroDTO>> listar(@RequestParam Long empresaId, @RequestParam Long operadoraId) {
        empresa = new Empresa();
        operadora = new Operadora();

        if ((empresaId != null && empresaId > 0) && (operadoraId != null && operadoraId > 0)) {
            empresa = empresaService.pesquisarPorId(empresaId);
            operadora = operadoraService.buscarPorID(operadoraId);
        }

        List<ParametroDTO> parametroDTOS = mapper.paraLista(TipoParametro.values(), empresa.getRazaoSocial(), operadora.getDescricao());
        List<Parametro> parametros = parametroService.pesquisar(empresa, operadora);

        return ResponseEntity.ok(mapper.substituir(parametros, parametroDTOS));
    }

    @PostMapping
    @PreAuthorize(Autorizar.ATIVAR_PARAMETRO)
    public ResponseEntity<ParametroDTO> ativar(@Valid @RequestBody ParametroDTO parametroDTO) {
        parametroDTO.setAtivo(!parametroDTO.getAtivo());
        Parametro parametro = mapper.paraEntidade(parametroDTO, empresa, operadora);
        return ResponseEntity.ok(mapper.paraDTO(parametroService.salvar(parametro)));
    }
}
