package com.nisum.test_jwt.client_register.application.service;

import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserRequestDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserResponseDto;

/**
 * Interface para el servicio de usuario.
 * Esta interfaz define el contrato para el servicio de registro de usuario.
 * 
 * Interface for UserServicePort.
 * This interface defines the contract for user registration service.
 */
public interface IUserService {
    UserResponseDto registerUser(UserRequestDto request);
}
