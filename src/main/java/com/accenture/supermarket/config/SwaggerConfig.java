package com.accenture.supermarket.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Supermarket Toshiro Shibakita",
                version = "1.0.0",
                description = "API para gerenciamento de produtos, clientes e usuários do supermercado.",
                contact = @Contact(
                        name = "Alex Viera Developer",
                        email = "alexvieira.360@hotmail.com",
                        url = "https://github.com/Alex-Vieira-da-Silva"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Documentação completa do projeto",
                url = "https://github.com/Alex-Vieira-da-Silva/supermarket.git"
        )
)
public class SwaggerConfig {
}
