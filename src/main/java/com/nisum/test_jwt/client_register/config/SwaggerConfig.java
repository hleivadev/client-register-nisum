package com.nisum.test_jwt.client_register.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;

/**
 * Configuracion class para SwaggerConfig.
 * Esta clase se utiliza para configurar la documentacion de la aplicacion.
 * 
 * Configuration class for SwaggerConfig.
 * This class is used to configure the documentation of the application.
 */
@Configuration
public class SwaggerConfig {
     @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Client Register API")
                .description("API para la creación de usuarios")
                .version("v1.0.0")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
            )
            .externalDocs(new ExternalDocumentation()
                .description("Repositorio GitHub")
                .url("https://github.com/hleivadev/client-register-provisorio"));
    }
}
