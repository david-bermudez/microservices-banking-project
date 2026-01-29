package com.banking.cuenta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.banking.cuenta.ratelimiter.RateLimitingProperty;

@SpringBootApplication
@EnableConfigurationProperties(RateLimitingProperty.class)
public class CuentaMovimientoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CuentaMovimientoServiceApplication.class, args);
    }
}
