package com.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.entity.User;

// Repository interface for User entity
public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom queries here if needed
}
