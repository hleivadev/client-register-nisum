package com.nisum.test_jwt.client_register.application.service;


import com.nisum.test_jwt.client_register.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationUserService implements IAuthenticationUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Iniciando búsqueda de usuario por email: {}", email);

        try {
            return userRepository.findByEmail(email)
                    .map(user -> {
                        if (!user.getIsActive()) {
                            log.warn("Usuario desactivado intentó loguearse : {}", email);
                            log.info("Usuario desactivado, acceso denegado: {}", user.getEmail());
                            throw new UsernameNotFoundException("Usuario desactivado, acceso denegado");
                        }
    
                        log.info("Usuario activo encontrado: {}", user.getEmail());
                        return org.springframework.security.core.userdetails.User
                                .withUsername(user.getEmail())
                                .password(user.getPassword())
                                .authorities("USER")
                                .build();
                    })
                    .orElseThrow(() -> {
                        log.warn("Usuario no encontrado con email: {}", email);
                        return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
                    });
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al cargar usuario con email: {}", email, e);
            throw new UsernameNotFoundException("Error interno al buscar usuario con email: " + email, e);
        }
    }
    
}