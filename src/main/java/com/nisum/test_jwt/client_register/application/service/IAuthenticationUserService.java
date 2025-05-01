package com.nisum.test_jwt.client_register.application.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IAuthenticationUserService extends UserDetailsService {

    /**
     * Carga los detalles del usuario a partir del email (username para Spring Security).
     * 
     * @param email el email del usuario
     * @return detalles del usuario
     * @throws UsernameNotFoundException si no se encuentra el usuario
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}

