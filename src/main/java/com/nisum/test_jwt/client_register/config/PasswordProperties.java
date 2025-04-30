package com.nisum.test_jwt.client_register.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion class para Password properties.
 * Esta clase se utiliza para enlazar las propiedades definidas en application.yml o application.properties
 * con el prefijo "password" a los campos de esta clase.
 * 
 * Configuration class for Password properties.
 * This class is used to bind the properties defined in application.yml or application.properties
 * with the prefix "password" to the fields in this class.
 */
@Configuration
@ConfigurationProperties(prefix = "password")
public class PasswordProperties {

    private String regex;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}

