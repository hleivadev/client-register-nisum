package com.nisum.test_jwt.client_register.infrastructure.adapter.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para solicitudes incorrectas (400).
 * Esta excepción se lanza cuando la solicitud del cliente no es válida.
 * 
 * Exception for bad requests (400).
 * This exception is thrown when the client's request is invalid.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
