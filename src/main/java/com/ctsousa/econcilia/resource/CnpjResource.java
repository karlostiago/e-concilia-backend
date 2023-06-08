package com.ctsousa.econcilia.resource;

import com.ctsousa.econcilia.assembler.CnpjAssembler;
import com.ctsousa.econcilia.assembler.EmpresaAssembler;
import com.ctsousa.econcilia.model.dto.EmpresaDTO;
import com.ctsousa.econcilia.service.CnpjService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cnpj")
public class CnpjResource {

    private final CnpjService cnpjService;

    private final CnpjAssembler cnpjAssembler;

    public CnpjResource(CnpjService cnpjService, CnpjAssembler cnpjAssembler) {
        this.cnpjService = cnpjService;
        this.cnpjAssembler = cnpjAssembler;
    }

    @GetMapping("{cnpj}")
    public ResponseEntity<EmpresaDTO> porCnpj (@PathVariable String cnpj) {
        var dadosCnpj = cnpjService.buscarCNPJ(cnpj);
        return ResponseEntity.ok(cnpjAssembler.paraDTO(dadosCnpj));
    }
}
