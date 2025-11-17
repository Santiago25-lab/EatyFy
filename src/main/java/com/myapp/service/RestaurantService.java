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
        List<Restaurant> restaurants = restaurantRepository.findAll();
        System.out.println("Found " + restaurants.size() + " restaurants in database");
        return restaurants;
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

    // Get restaurants by owner
    public List<Restaurant> getRestaurantsByOwner(Long ownerId) {
        return restaurantRepository.findAll().stream()
                .filter(r -> r.getOwner() != null && r.getOwner().getId().equals(ownerId))
                .toList();
    }
}
