package com.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.entity.User;
import com.myapp.repository.UserRepository;

// Service to handle User entity operations
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // Save a new user or update an existing user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get user by id
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Delete user by id
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
