package com.ctsousa.econcilia.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    private static final String ORIGIN = "Origin";
    private final String origensPermitida = "http://localhost:4200, https://localhost:4200, https://e-concilia.herokuapp.com, http://e-concilia.herokuapp.com";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Origin", origemPermitida(request));
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equals(request.getMethod()) && temOrigemPermitida(request)) {
            log.info("Adicinoando header a requisição ::: ");

            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, PATCH, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            response.setHeader("Access-Control-Max-Age", "3600");

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String origemPermitida(final HttpServletRequest request) {
        String[] origens = origensPermitida.split(",");
        String origemPermitida = "http://localhost:4200";

        for (String origem : origens) {
            if (request.getHeader(ORIGIN) != null && request.getHeader(ORIGIN).equals(origem.trim())) {
                origemPermitida = origem;
                break;
            }
        }

        log.info("Origem permitida ::: [{}], path origem [{}]", origemPermitida, request.getHeader(ORIGIN));

        return origemPermitida;
    }

    private boolean temOrigemPermitida(final HttpServletRequest request) {
        return origemPermitida(request) != null;
    }
}
