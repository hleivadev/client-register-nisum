package com.nisum.test_jwt.client_register.domain.repository;

import com.nisum.test_jwt.client_register.domain.model.User;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

}