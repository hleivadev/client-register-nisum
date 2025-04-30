package com.nisum.test_jwt.client_register.infrastructure.adapter.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para recursos no encontrados (404).
 * Esta excepción se lanza cuando un recurso solicitado no se encuentra en el servidor.
 * 
 * Exception for not found resources (404).
 * This exception is thrown when a requested resource is not found on the server.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    
    public NotFoundException(String message) {
        super(message);
    }
}
