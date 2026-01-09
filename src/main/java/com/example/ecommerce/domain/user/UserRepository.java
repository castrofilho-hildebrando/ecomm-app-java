package com.example.ecommerce.domain.user;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(UserId id);

    void save(User user);
}
