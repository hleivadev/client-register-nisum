package com.nisum.test_jwt.client_register.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion class for JWT properties.
 * Esta clase se utiliza para enlazar las propiedades definidas en application.yml o application.properties
 * con el prefijo "jwt" a los campos de esta clase.
 * 
 * Configuration class for JWT properties.
 * This class is used to bind the properties defined in application.yml or application.properties
 * with the prefix "jwt" to the fields in this class.
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long expiration;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
