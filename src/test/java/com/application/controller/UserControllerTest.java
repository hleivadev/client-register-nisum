package com.application.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.test_jwt.client_register.application.service.UserService;
import com.nisum.test_jwt.client_register.config.TestSecurityConfig;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.controller.UserController;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.PhoneDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserRequestDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserResponseDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.exception.BadRequestException;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.exception.ConflictException;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.exception.NotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@ContextConfiguration(classes = com.nisum.test_jwt.client_register.ClientRegisterApplication.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_Successful() throws Exception {
        UserRequestDto request = new UserRequestDto(
                "Juan Rodriguez",
                "juan@rodriguez.org",
                "Password123",
                Collections.singletonList(new PhoneDto("1234567", "1", "57"))
        );

        UserResponseDto response = UserResponseDto.builder()
                .id(UUID.randomUUID())
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .token("fake-jwt-token")
                .phones(Collections.singletonList(new PhoneDto("1234567", "1", "57")))
                .build();

        Mockito.when(userService.registerUser(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan@rodriguez.org"))
                .andExpect(jsonPath("$.name").value("Juan Rodriguez"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void registerUser_WhenPasswordInvalid_Returns400() throws Exception {
        UserRequestDto request = new UserRequestDto(
                "Juan",
                "juan@rodriguez.org",
                "weakpass",
                Collections.singletonList(new PhoneDto("1234567", "1", "57"))
        );

        Mockito.when(userService.registerUser(Mockito.any()))
                .thenThrow(new BadRequestException("La contraseña no cumple con el formato requerido"));

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La contraseña no cumple con el formato requerido"));
    }

    @Test
    void registerUser_WhenEmailAlreadyExists_Returns409() throws Exception {
        UserRequestDto request = new UserRequestDto(
                "Juan",
                "juan@rodriguez.org",
                "Password123",
                Collections.singletonList(new PhoneDto("1234567", "1", "57"))
        );

        Mockito.when(userService.registerUser(Mockito.any()))
                .thenThrow(new ConflictException("El correo ya registrado"));

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El correo ya registrado"));
    }

    @Test
    void registerUser_WhenUserNotFound_Returns404() throws Exception {
        UserRequestDto request = new UserRequestDto(
                "Juan",
                "no@existe.com",
                "Password123",
                Collections.singletonList(new PhoneDto("1234567", "1", "57"))
        );

        Mockito.when(userService.registerUser(Mockito.any()))
                .thenThrow(new NotFoundException("Usuario no encontrado"));

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Usuario no encontrado"));
    }

    @Test
    void registerUser_WhenUnexpectedError_Returns500() throws Exception {
        UserRequestDto request = new UserRequestDto(
                "Juan",
                "juan@rodriguez.org",
                "Password123",
                Collections.singletonList(new PhoneDto("1234567", "1", "57"))
        );

        Mockito.when(userService.registerUser(Mockito.any()))
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje").value("Error inesperado"));
    }
}
