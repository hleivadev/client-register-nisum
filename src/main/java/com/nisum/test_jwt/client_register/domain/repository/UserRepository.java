package com.nisum.test_jwt.client_register.domain.repository;

import com.nisum.test_jwt.client_register.domain.model.User;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz para el repositorio de usuarios.
 * Esta interfaz define los métodos para guardar y buscar usuarios en la base de datos.
 * 
 * Interface for the user repository.
 * This interface defines the methods to save and find users in the database.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

}