package com.example.postspringsecurity.auth.repositories;

import com.example.postspringsecurity.auth.models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, String> {
    Users findByUsername(String username);
}
