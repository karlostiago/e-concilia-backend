package com.ctsousa.econcilia.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig implements Filter {

    private static final String OPTIONS = "OPTIONS";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    private static final String METHODS = "POST, GET, DELETE, PUT, OPTIONS";
    private static final String HEADERS = "Authorization, Content-Type, Accept";
    private static final String MAX_AGE = "3600";
    private static final String CREDENTIALS = "true";
    private static final String ORIGIN = "Origin";
    private static final String ORIGIN_DEFAULT = "http://localhost:4200";

    private final String[] origensPermitidas = {"http://localhost:4200", "https://localhost:4200", "https://e-concilia.herokuapp.com", "http://e-concilia.herokuapp.com"};

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "https://e-concilia.herokuapp.com");
        response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, CREDENTIALS);

        if(OPTIONS.equals(request.getMethod()) && temOriginPermitida(request)) {
            response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, METHODS);
            response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, HEADERS);
            response.setHeader(ACCESS_CONTROL_MAX_AGE, MAX_AGE);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            chain.doFilter(request, response);
        }
    }

    private String getOrigemPermitida(HttpServletRequest request) {
        for(String origemPermitida : this.origensPermitidas) {
            if(request.getHeader(ORIGIN) != null && request.getHeader(ORIGIN).equals(origemPermitida.trim())) {
                return origemPermitida;
            }
        }
        return ORIGIN_DEFAULT;
    }

    private boolean temOriginPermitida(HttpServletRequest request) {
        boolean temOrigemPermitida = false;

        for(String origemPermitida : this.origensPermitidas) {
            if(request.getHeader(ORIGIN).equals(origemPermitida.trim())) {
                temOrigemPermitida = true;
                break;
            }
        }

        return temOrigemPermitida;
    }
}

