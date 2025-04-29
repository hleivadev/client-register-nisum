package com.nisum.test_jwt.client_register.infrastructure.adapter.repository;

import com.nisum.test_jwt.client_register.domain.model.User;
import com.nisum.test_jwt.client_register.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID>, UserRepository {

    @Override
    Optional<User> findByEmail(String email);

}
