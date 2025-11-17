package com.myapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.entity.MenuItem;
import com.myapp.entity.Restaurant;
import com.myapp.entity.User;
import com.myapp.service.GeocodingService;
import com.myapp.service.MenuItemService;
import com.myapp.service.RestaurantService;
import com.myapp.service.UserService;

// Controller for handling Restaurant-related HTTP requests
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    private MenuItemService menuItemService;

    // Get all restaurants from database
    @GetMapping
    public List<Restaurant> getRestaurants(@RequestParam(required = false) String city,
                                           @RequestParam(required = false) Double budget) {
        List<Restaurant> restaurants = restaurantService.getRestaurants();

        if (city != null) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getAddress() != null &&
                               r.getAddress().toLowerCase().contains(city.toLowerCase()))
                    .toList();
        }

        if (budget != null) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getAveragePricePerPerson() != null &&
                               r.getAveragePricePerPerson() <= budget)
                    .toList();
        }

        return restaurants;
    }

    // Get current user's restaurants
    @GetMapping("/my")
    public List<Restaurant> getMyRestaurants(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);
        if (user == null) {
            return List.of();
        }
        return restaurantService.getRestaurantsByOwner(user.getId());
    }

    // Get restaurants by city (legacy endpoint for frontend compatibility)
    @GetMapping("/search")
    public List<Map<String, Object>> getRestaurantsByCity(@RequestParam(required = false) String city,
                                                           @RequestParam(required = false) Double budget) {
        List<Restaurant> restaurants = restaurantService.getRestaurants();

        if (city != null) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getAddress() != null &&
                                r.getAddress().toLowerCase().contains(city.toLowerCase()))
                    .toList();
        }

        if (budget != null) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getAveragePricePerPerson() != null &&
                                r.getAveragePricePerPerson() <= budget)
                    .toList();
        }

        // Convert to frontend format with menu items
        return restaurants.stream().<Map<String, Object>>map(r -> {
            // Get menu items for this restaurant
            List<MenuItem> menuItems = menuItemService.getMenuItemsByRestaurant(r.getId());

            // Filter menu items by budget if specified
            List<Map<String, Object>> affordableMenuItems = menuItems.stream()
                .filter(item -> budget == null || item.getPrice() == null || item.getPrice() <= budget)
                .map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("id", item.getId());
                    itemMap.put("name", item.getName());
                    itemMap.put("description", item.getDescription());
                    itemMap.put("price", item.getPrice());
                    itemMap.put("category", item.getCategory());
                    return itemMap;
                })
                .collect(java.util.stream.Collectors.toList());

            Map<String, Object> restaurantMap = new HashMap<>();
            restaurantMap.put("id", r.getId().toString());
            restaurantMap.put("name", r.getName());
            restaurantMap.put("address", r.getAddress());
            restaurantMap.put("city", city != null ? city : "Bogotá");
            restaurantMap.put("lat", r.getLatitude() != null ? r.getLatitude().doubleValue() : 4.711);
            restaurantMap.put("lon", r.getLongitude() != null ? r.getLongitude().doubleValue() : -74.0721);
            restaurantMap.put("cuisine", r.getCuisineType() != null ? r.getCuisineType() : "No especificado");
            restaurantMap.put("phone", r.getPhone());
            restaurantMap.put("website", r.getWebsite());
            restaurantMap.put("priceRange", r.getAveragePricePerPerson() != null ?
                (r.getAveragePricePerPerson() < 30000 ? "$" :
                 r.getAveragePricePerPerson() < 60000 ? "$$" : "$$$") : "$$");
            restaurantMap.put("menuItems", affordableMenuItems);

            return restaurantMap;
        }).toList();
    }

    // Create a new restaurant (for authenticated users)
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantRegistrationRequest request, Authentication authentication) {
        System.out.println("=== CREATE RESTAURANT ENDPOINT CALLED ===");
        String email = authentication.getName();
        System.out.println("Creating restaurant for user: " + email);
        User user = userService.findByEmail(email).orElse(null);
        if (user == null) {
            System.out.println("User not found: " + email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        System.out.println("User found: " + user.getId() + " - " + user.getName());

        // Create restaurant entity from request
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setPhone(request.getPhone());
        restaurant.setWebsite(request.getWebsite());
        restaurant.setOpeningHours(request.getOpeningHours());
        restaurant.setAveragePricePerPerson(request.getAveragePricePerPerson());
        restaurant.setLatitude(request.getLatitude());
        restaurant.setLongitude(request.getLongitude());
        restaurant.setOwner(user);

        // Get coordinates from address using OpenStreetMap (optional)
        if (restaurant.getAddress() != null && !restaurant.getAddress().trim().isEmpty()) {
            try {
                System.out.println("Attempting to geocode address: " + restaurant.getAddress());
                Map<String, Double> coordinates = geocodingService.getCoordinates(restaurant.getAddress() + ", Colombia");
                if (coordinates != null && coordinates.get("lat") != null && coordinates.get("lon") != null) {
                    restaurant.setLatitude(coordinates.get("lat"));
                    restaurant.setLongitude(coordinates.get("lon"));
                    System.out.println("Geocoding successful: lat=" + coordinates.get("lat") + ", lon=" + coordinates.get("lon"));
                } else {
                    System.out.println("No coordinates found for address: " + restaurant.getAddress());
                }
            } catch (Exception e) {
                // Log error but don't fail restaurant creation
                System.err.println("Error geocoding address: " + restaurant.getAddress() + " - " + e.getMessage());
            }
        }

        System.out.println("Saving restaurant: " + restaurant.getName() + " for user: " + user.getId());
        Restaurant saved = restaurantService.saveRestaurant(restaurant);
        System.out.println("Restaurant saved with ID: " + saved.getId());

        // Create menu items if provided
        if (request.getMenuItems() != null && !request.getMenuItems().isEmpty()) {
            for (RestaurantRegistrationRequest.MenuItemRequest itemRequest : request.getMenuItems()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setName(itemRequest.getName());
                menuItem.setDescription(itemRequest.getDescription());
                menuItem.setPrice(itemRequest.getPrice());
                menuItem.setCategory(itemRequest.getCategory());
                menuItem.setRestaurant(saved);

                menuItemService.saveMenuItem(menuItem);
                System.out.println("Menu item saved: " + menuItem.getName());
            }
        }

        return ResponseEntity.ok(saved);
    }

    // Get restaurant by id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }

        // Get menu items for this restaurant
        List<MenuItem> menuItems = menuItemService.getMenuItemsByRestaurant(id);
        List<Map<String, Object>> menuItemsResponse = menuItems.stream()
            .map(item -> {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("id", item.getId());
                itemMap.put("name", item.getName());
                itemMap.put("description", item.getDescription());
                itemMap.put("price", item.getPrice());
                itemMap.put("category", item.getCategory());
                return itemMap;
            })
            .collect(java.util.stream.Collectors.toList());

        // Create response with restaurant and menu items
        Map<String, Object> response = new HashMap<>();
        response.put("id", restaurant.getId());
        response.put("name", restaurant.getName());
        response.put("address", restaurant.getAddress());
        response.put("cuisineType", restaurant.getCuisineType());
        response.put("phone", restaurant.getPhone());
        response.put("website", restaurant.getWebsite());
        response.put("openingHours", restaurant.getOpeningHours());
        response.put("averagePricePerPerson", restaurant.getAveragePricePerPerson());
        response.put("latitude", restaurant.getLatitude());
        response.put("longitude", restaurant.getLongitude());
        response.put("menuItems", menuItemsResponse);

        return ResponseEntity.ok(response);
    }

    // Update restaurant (owner only)
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant updatedRestaurant, Authentication authentication) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);
        if (user == null || !restaurant.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Update coordinates if address changed
        if (!restaurant.getAddress().equals(updatedRestaurant.getAddress())) {
            Map<String, Double> coordinates = geocodingService.getCoordinates(updatedRestaurant.getAddress() + ", Colombia");
            if (coordinates != null) {
                updatedRestaurant.setLatitude(coordinates.get("lat"));
                updatedRestaurant.setLongitude(coordinates.get("lon"));
            }
        }

        updatedRestaurant.setId(id);
        updatedRestaurant.setOwner(user);
        Restaurant saved = restaurantService.saveRestaurant(updatedRestaurant);
        return ResponseEntity.ok(saved);
    }

    // Delete restaurant by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id, Authentication authentication) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);
        if (user == null || !restaurant.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
