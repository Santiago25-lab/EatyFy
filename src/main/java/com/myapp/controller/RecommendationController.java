package com.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.entity.Restaurant;
import com.myapp.entity.User;
import com.myapp.service.RecommendationService;
import com.myapp.service.UserService;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getPersonalizedRecommendations(
            Authentication authentication,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double budget) {

        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Restaurant> recommendations = recommendationService.getRecommendationsByCity(city, user, budget);
        return ResponseEntity.ok(recommendations);
    }
}