package com.ctsousa.econcilia.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class SpringConfig {

    private static ApplicationContext applicationContext;

    @Autowired
    @SuppressWarnings("static")
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringConfig.applicationContext = applicationContext;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public CacheManager cacheManager() {
        return new CaffeineCacheManager();
    }

    public static Object getBean(String bean) {
        return applicationContext.getBean(bean);
    }
}
