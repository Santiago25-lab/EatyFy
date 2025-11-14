package com.myapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.entity.Restaurant;
import com.myapp.entity.User;
import com.myapp.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RecommendationService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Restaurant> getPersonalizedRecommendations(User user, Double budget) {
        List<Restaurant> allRestaurants = restaurantRepository.findAll();

        // Filter by budget if provided
        if (budget != null && budget > 0) {
            allRestaurants = allRestaurants.stream()
                    .filter(r -> r.getAveragePricePerPerson() != null && r.getAveragePricePerPerson() <= budget)
                    .collect(Collectors.toList());
        }

        // If user has preferences, use them for recommendations
        if (user.getFoodPreferences() != null && !user.getFoodPreferences().isEmpty()) {
            try {
                JsonNode preferences = objectMapper.readTree(user.getFoodPreferences());
                String favoriteCuisine = preferences.get("favoriteCuisine").asText();
                String dietaryRestrictions = preferences.get("dietaryRestrictions").asText();

                // Prioritize restaurants matching favorite cuisine
                if (favoriteCuisine != null && !favoriteCuisine.equals("null") && !favoriteCuisine.isEmpty()) {
                    List<Restaurant> matchingCuisine = allRestaurants.stream()
                            .filter(r -> r.getCuisineType() != null &&
                                       r.getCuisineType().toLowerCase().contains(favoriteCuisine.toLowerCase()))
                            .collect(Collectors.toList());

                    // If we have matches, prioritize them, otherwise return all filtered
                    if (!matchingCuisine.isEmpty()) {
                        // Add non-matching restaurants at the end with lower priority
                        List<Restaurant> nonMatching = allRestaurants.stream()
                                .filter(r -> !matchingCuisine.contains(r))
                                .collect(Collectors.toList());
                        matchingCuisine.addAll(nonMatching);
                        return matchingCuisine;
                    }
                }

                // TODO: Handle dietary restrictions in menu items when menu system is more developed

            } catch (Exception e) {
                // If JSON parsing fails, continue with basic recommendations
                System.err.println("Error parsing user preferences: " + e.getMessage());
            }
        }

        // Return restaurants sorted by average price (cheapest first for budget-conscious users)
        return allRestaurants.stream()
                .sorted((r1, r2) -> {
                    Double p1 = r1.getAveragePricePerPerson() != null ? r1.getAveragePricePerPerson() : Double.MAX_VALUE;
                    Double p2 = r2.getAveragePricePerPerson() != null ? r2.getAveragePricePerPerson() : Double.MAX_VALUE;
                    return p1.compareTo(p2);
                })
                .collect(Collectors.toList());
    }

    public List<Restaurant> getRecommendationsByCity(String city, User user, Double budget) {
        List<Restaurant> recommendations = getPersonalizedRecommendations(user, budget);

        // Filter by city if specified
        if (city != null && !city.isEmpty()) {
            recommendations = recommendations.stream()
                    .filter(r -> r.getAddress() != null &&
                               r.getAddress().toLowerCase().contains(city.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return recommendations;
    }
}