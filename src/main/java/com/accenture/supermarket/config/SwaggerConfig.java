package com.accenture.supermarket.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Supermarket",
                version = "1.0",
                description = "API para gerenciamento de produtos do supermercado"
        )
)
public class SwaggerConfig {
}