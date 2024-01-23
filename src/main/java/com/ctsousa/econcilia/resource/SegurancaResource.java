package com.ctsousa.econcilia.resource;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seguranca")
public class SegurancaResource {

    @GetMapping("/login")
    public void login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var usuario = auth.getName();
        System.out.println(auth.getPrincipal());
        System.out.println(usuario);
    }
}
