package com.nisum.test_jwt.client_register.infrastructure.adapter.rest.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.nisum.test_jwt.client_register.application.service.UserService;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserRequestDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "Operaciones para registro de usuarios")
public class UserController {

    // inyeccion de servicio userService || injection of userService
    private final UserService userService;

    // constructor || constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Metodo para registrar un nuevo usuario || Method to register a new user
     * 
     * @param request
     * @return ResponseEntity<UserResponseDto>
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto request) {
        log.debug("Iniicio controller registerUser() || Start controller registerUser()");
        UserResponseDto response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Metodo para obtener el name del usuario autenticado ||
     * Method to get the name of the authenticated user
     * 
     * @param authentication
     * @return ResponseEntity<Map<String, String>>
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getAuthenticatedUser(Authentication authentication) {
        String name = authentication.getName();
        return ResponseEntity.ok(Map.of("name_authenticated: ", name));
    }
}
