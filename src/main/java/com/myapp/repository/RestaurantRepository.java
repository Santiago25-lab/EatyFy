package com.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.entity.Restaurant;

// Repository interface for Restaurant entity
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // You can add custom queries here if needed
}
