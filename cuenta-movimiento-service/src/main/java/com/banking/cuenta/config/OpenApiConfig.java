package com.banking.cuenta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI cuentaMovimientoOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Cuenta Movimiento Service API")
                                                .description("Microservicio de gestión de cuentas y movimientos bancarios. "
                                                                +
                                                                "Proporciona operaciones para administrar cuentas, registrar movimientos y generar reportes.")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("David Bermudez")
                                                                .email("dbermudez@test.com"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
        }
}
