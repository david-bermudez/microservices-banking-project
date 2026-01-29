package com.banking.cliente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.banking.cliente.ratelimiter.RateLimitingProperty;

@SpringBootApplication
@EnableConfigurationProperties(RateLimitingProperty.class)
public class ClientePersonaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientePersonaServiceApplication.class, args);
    }
}
