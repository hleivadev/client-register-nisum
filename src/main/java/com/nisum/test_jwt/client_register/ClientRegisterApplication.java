package com.nisum.test_jwt.client_register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.nisum.test_jwt.client_register.config.JwtProperties;
import com.nisum.test_jwt.client_register.config.PasswordProperties;

@EnableConfigurationProperties({JwtProperties.class, PasswordProperties.class})
@SpringBootApplication
public class ClientRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientRegisterApplication.class, args);
	}

}
