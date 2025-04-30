package com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class for User response.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserResponseDto {

    protected final UUID id;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastLogin;
    protected final String token;
    protected final Boolean isActive;
    protected final String email;
    protected final String name;
    protected final List<PhoneDto> phones;
    
}
