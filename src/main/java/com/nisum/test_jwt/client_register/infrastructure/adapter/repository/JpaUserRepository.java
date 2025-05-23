package com.nisum.test_jwt.client_register.infrastructure.adapter.repository;

import com.nisum.test_jwt.client_register.domain.model.User;
import com.nisum.test_jwt.client_register.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JpaUserRepository interface para User entity.
 * extiende JpaRepository para proporcionar operaciones CRUD y métodos de consulta personalizados.
 * 
 * JpaUserRepository interface for User entity.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID>, UserRepository {

    /**
     * Find a User by email. 
     *
     * encuentra un User por email.
     * 
     * @param email the email of the User
     * @return an Optional containing the User if found, or empty if not found
     */
    @Override
    Optional<User> findByEmail(String email);

}
