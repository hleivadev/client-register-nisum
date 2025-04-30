package com.nisum.test_jwt.client_register.infrastructure.adapter.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para conflictos (409).
 * Esta excepción se lanza cuando hay un conflicto en la solicitud del cliente.
 * 
 * Exception for conflicts (409).
 * This exception is thrown when there is a conflict in the client's request.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}