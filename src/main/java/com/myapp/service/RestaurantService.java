package com.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.entity.Restaurant;
import com.myapp.repository.RestaurantRepository;

// Service to handle Restaurant entity operations
@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Get all restaurants
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    // Save a new restaurant or update an existing restaurant
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // Get restaurant by id
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    // Delete restaurant by id
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}
