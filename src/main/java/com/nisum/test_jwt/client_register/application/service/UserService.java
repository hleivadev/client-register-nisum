package com.nisum.test_jwt.client_register.application.service;

import com.nisum.test_jwt.client_register.config.PasswordProperties;
import com.nisum.test_jwt.client_register.domain.model.Phone;
import com.nisum.test_jwt.client_register.domain.model.User;
import com.nisum.test_jwt.client_register.domain.repository.UserRepository;

import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.PhoneDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserRequestDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserResponseDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.security.JwtUtil;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {

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

    @Transactional
    public UserResponseDto registerUser(UserRequestDto request) {
        // Validar si el email ya existe || validation if email exist already
        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("El correo ya registrado || email already exist");
                });

        // Validar formato de password || validation password format
        if (!passwordPattern.matcher(request.getPassword()).matches()) {
            throw new RuntimeException("La contraseña no cumple con el formato requerido || password doesnt have the correct format");
        }

        // Mapear PhoneDto a Phone || map phoneDto to Phone
        List<Phone> phones = request.getPhones().stream()
                .map(this::mapToPhone)
                .collect(Collectors.toList());

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

        user.setPhones(phones);

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
                .build();
    }

    private Phone mapToPhone(PhoneDto phoneDto) {
        Phone phone = new Phone();
        phone.setNumber(phoneDto.getNumber());
        phone.setCityCode(phoneDto.getCityCode());
        phone.setCountryCode(phoneDto.getCountryCode());
        return phone;
    }

    @PostConstruct
    public void init() {
        System.out.println("[UserService] Password Regex Loaded: " + passwordPattern.pattern());
    }
}
