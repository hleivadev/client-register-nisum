package com.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nisum.test_jwt.client_register.application.service.UserService;
import com.nisum.test_jwt.client_register.config.PasswordProperties;
import com.nisum.test_jwt.client_register.domain.model.Phone;
import com.nisum.test_jwt.client_register.domain.model.User;
import com.nisum.test_jwt.client_register.domain.repository.UserRepository;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.PhoneDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserRequestDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.rest.dto.UserResponseDto;
import com.nisum.test_jwt.client_register.infrastructure.adapter.security.JwtUtil;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordProperties passwordProperties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(passwordProperties.getRegex()).thenReturn("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

        userService = new UserService(userRepository, jwtUtil, passwordEncoder, passwordProperties);

        when(jwtUtil.generateToken(anyString())).thenReturn("fake-jwt-token");
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-password123");
    }

    @Test
    public void testRegisterUserSuccessfully() {

        String rawPassword = generateValidPassword();

        // Mock data
        Phone phone = new Phone("123456789", "01", "56");
        List<Phone> phones = List.of(phone);

        UUID expectedUserId = UUID.randomUUID();

        User newUser = new User(
                "Héctor Leiva",
                "hector@example.com",
                rawPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                "fake-jwt-token",
                phones);
        newUser.setId(expectedUserId);

        UserRequestDto userRequest = new UserRequestDto();
        userRequest.setName(newUser.getName());
        userRequest.setEmail(newUser.getEmail());
        userRequest.setPassword(newUser.getPassword());
        List<PhoneDto> phoneDtos = phones.stream()
                .map(p -> new PhoneDto(p.getNumber(), p.getCityCode(), p.getCountryCode()))
                .toList();
        userRequest.setPhones(phoneDtos);

        // Mocking the behavior of the repository
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Call the method to test
        UserResponseDto result = userService.registerUser(userRequest);

        assertNotNull(result);
        assertEquals("hector@example.com", result.getEmail());
        // Validations
        assertNotNull(result);
        assertEquals("Héctor Leiva", result.getName());
        assertEquals(expectedUserId, result.getId());
        assertEquals("fake-jwt-token", result.getToken());
        assertTrue(result.getIsActive());
        assertNotNull(result.getCreated());
        assertNotNull(result.getModified());
        assertNotNull(result.getLastLogin());
        assertEquals(1, result.getPhones().size());
        verify(userRepository, times(1)).save(any(User.class));
    }

    private String generateValidPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String all = upper + lower + digits;

        StringBuilder password = new StringBuilder();
        password.append(upper.charAt((int) (Math.random() * upper.length())));
        password.append(lower.charAt((int) (Math.random() * lower.length())));
        password.append(digits.charAt((int) (Math.random() * digits.length())));

        for (int i = 3; i < 10; i++) {
            password.append(all.charAt((int) (Math.random() * all.length())));
        }

        return password.toString();
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {

        String emailDuplicado = "duplicado@example.com";
        String rawPassword = generateValidPassword();

        Phone phone = new Phone("123456789", "01", "56");
        List<Phone> phones = List.of(phone);

        User existingUser = new User(
                "Usuario Existente",
                emailDuplicado,
                rawPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                "token-ya-generado",
                phones);
        existingUser.setId(UUID.randomUUID());

        UserRequestDto request = new UserRequestDto();
        request.setName("Nuevo Usuario");
        request.setEmail(emailDuplicado);
        request.setPassword(rawPassword);
        request.setPhones(List.of(new PhoneDto("123456789", "01", "56")));

        when(userRepository.findByEmail(emailDuplicado)).thenReturn(Optional.of(existingUser));

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("El correo ya registrado || email already exist", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(emailDuplicado);
        verify(userRepository, never()).save(any(User.class));
    }

}
