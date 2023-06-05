package com.ctsousa.econcilia.resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/contratos")
public class ContratoResource {

    @PostMapping
    public void cadastrar () {
        System.out.println("Servidor rodando....");
    }
}
