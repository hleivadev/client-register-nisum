package com.nisum.test_jwt.client_register.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nisum.test_jwt.client_register.application.service.IAuthenticationUserService;
import com.nisum.test_jwt.client_register.infrastructure.adapter.security.JwtAuthenticationFilter;

/**
 * Configuracion class para Spring Security.
 * Esta clase se utiliza para configurar la seguridad de la aplicacion.
 * 
 * Configuration class for Spring Security.
 * This class is used to configure the security of the application.
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configuracion del filtro de seguridad.
     * Este metodo configura el filtro de seguridad para la aplicacion.
     * 
     * Security filter chain configuration.
     * This method configures the security filter for the application.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/v1/users/register",
                    "/api/v1/auth/login",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/webjars/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 👈 JWT = sin sesión
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)   // 👈 Aplica filtro antes del de login clásico
            .build();
    }

    /**
     * Provee el AuthenticationManager desde la configuración.
     * Provides the AuthenticationManager from configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
