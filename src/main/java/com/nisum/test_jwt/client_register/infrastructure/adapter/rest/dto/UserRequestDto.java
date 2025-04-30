package com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank(message = "El nombre es obligatorio || the name is obligatory")
    private String name;

    @NotBlank(message = "El correo es obligatorio || the email is obligatory")
    @Email(message = "El formato del correo es inválido || the format is wrong")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria || the password is obligatory")
    private String password;

    @NotEmpty(message = "Debe existir al menos un teléfono || must be at least a number phone")
    private List<PhoneDto> phones;

    // Default constructor
    public UserRequestDto() {
    }

    // Constructor with parameters
    public UserRequestDto(String name, String email, String password, List<PhoneDto> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    
}
