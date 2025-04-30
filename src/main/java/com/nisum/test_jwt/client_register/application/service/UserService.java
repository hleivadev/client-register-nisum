package com.nisum.test_jwt.client_register.application.service;

import com.nisum.test_jwt.client_register.config.PasswordProperties;
import com.nisum.test_jwt.client_register.domain.model.Phone;
import com.nisum.test_jwt.client_register.domain.model.User;
import com.nisum.test_jwt.client_register.domain.repository.UserRepository;

import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.PhoneDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserRequestDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserResponseDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.exception.BadRequestException;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.exception.ConflictException;
import com.nisum.test_jwt.client_register.infrastructure.adapter.security.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final Pattern passwordPattern;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
            PasswordProperties passwordProperties) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.passwordPattern = Pattern.compile(passwordProperties.getRegex());
    }

    /**
     * Metodo para registrar un nuevo usuario || Method to register a new user
     * 
     * @param request
     * @return UserResponseDto
     */
    @Transactional
    public UserResponseDto registerUser(UserRequestDto request) {
        log.info("Inicio method registerUser: Registrando usuario ");


        // Validar si el email ya existe || validation if email exist already
        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new ConflictException("El correo ya registrado || email already exist");
                });

        // Validar formato de password || validation password format
        if (!passwordPattern.matcher(request.getPassword()).matches()) {
            throw new BadRequestException(
                    "La contraseña no cumple con el formato requerido || Password doesnt have the correct format");
        }

        // Crear usuario || create user
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(true);

        // Generar token || token generate
        String token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);

        user.setPhones(request.getPhones().stream()
                .map(phoneDto -> new Phone(phoneDto.getNumber(), phoneDto.getCityCode(), phoneDto.getCountryCode())) // ejemplo de uso de stream().map() and collect() || example of use of stream().map() and collect()
                .collect(Collectors.toList()));

        // Guardar usuario || save user
        User savedUser = userRepository.save(user);

        // Retornar respuesta || response returned
        return UserResponseDto.builder()
                .id(savedUser.getId())
                .created(savedUser.getCreated())
                .modified(savedUser.getModified())
                .lastLogin(savedUser.getLastLogin())
                .isActive(savedUser.getIsActive())
                .token(savedUser.getToken())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .phones(savedUser.getPhones().stream()
                        .map(this::toPhoneDto)// ejemplo de uso de metodo toPhoneDto || example of use of method toPhoneDto
                        .collect(Collectors.toList()))
                .build();

    }

    /**
     * Metodo para convertir un objeto Phone a PhoneDto || creado para cuando el
     * servicio crece y se necesita transformar de Dto a object o viceversa
     * Method to convert a Phone object to PhoneDto || created for when the service
     * grows and needs to transform from Dto to object or vice versa
     * 
     * @param phone
     * @return PhoneDto
     */
    private PhoneDto toPhoneDto(Phone phone) {
        return new PhoneDto(phone.getNumber(), phone.getCityCode(), phone.getCountryCode());
    }

    /**
     * Metodo para convertir un objeto PhoneDto a Phone || creado para cuando el
     * servicio crece y se necesita transformar de Dto a object o viceversa
     * Method to convert a PhoneDto object to Phone || created for when the service
     * grows and needs to transform from Dto to object or vice versa
     * 
     * @param phoneDto
     * @return Phone
     */
    private Phone toPhone(PhoneDto phoneDto) {
        return new Phone(phoneDto.getNumber(), phoneDto.getCityCode(), phoneDto.getCountryCode());
    }

}
