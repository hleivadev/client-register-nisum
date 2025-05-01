package com.nisum.test_jwt.client_register.infrastructure.adapter.rest.controller;

import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.LoginRequestDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.LoginResponseDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.exception.BadRequestException;
import com.nisum.test_jwt.client_register.infrastructure.adapter.security.JwtUtil;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint para login de usuario.
     * Devuelve un token JWT si las credenciales son válidas.
     */
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequest) {
        log.info("Intentando login con email: {}", loginRequest.getEmail());

        try {
            // Autenticación del usuario
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            String token = jwtUtil.generateToken(loginRequest.getEmail());
            log.info("Login exitoso para el usuario: {}", loginRequest.getEmail());
            return new LoginResponseDto(token, authentication.getName());

        } catch (AuthenticationException e) {
            log.warn("Credenciales inválidas para: {}", loginRequest.getEmail());
            throw new BadRequestException("Email o contraseña incorrectos");
        } catch (Exception e) {
            log.error("Error inesperado durante login para: {}", loginRequest.getEmail(), e);
            throw new RuntimeException("Error interno en el login");
        }
    }
}
