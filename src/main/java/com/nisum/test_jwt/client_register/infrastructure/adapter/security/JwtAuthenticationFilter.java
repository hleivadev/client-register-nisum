package com.nisum.test_jwt.client_register.infrastructure.adapter.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filter that checks for JWT tokens in the Authorization header of incoming requests.
 * If a valid token is found, it sets the authentication in the security context.
 * Filtro que verifica los tokens JWT en el header Authorization de las peticiones entrantes.
 * Si se encuentra un token valido, establece la autenticacion en el contexto de seguridad.
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Check if the Authorization header is present and starts with "Bearer "
        // Verifica si el header Authorization está presente y empieza con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                log.warn("Error al extraer el username desde el token: {}", e.getMessage());
            }
        }

        // If the username is not null and the authentication context is empty, load the user details
        // Si el username no es nulo y el contexto de autenticacion está vacío, carga los detalles del usuario
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the JWT token and set the authentication in the security context
            // Valida el token JWT y establece la autenticacion en el contexto de seguridad
            if (jwtUtil.validateToken(jwt)) {
                // Create an authentication token with the user details and set it in the security context
                // Crea un token de autenticacion con los detalles del usuario y lo establece en el contexto de seguridad
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set the authentication token in the security context
                // Establece el token de autenticacion en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Autenticación establecida para usuario: {}", username);
            } else {
                log.warn("Token inválido para el usuario: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
