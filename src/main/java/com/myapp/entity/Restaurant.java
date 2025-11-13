package com.myapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Entity: Represents a restaurant in the app
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the restaurant

    private String name;        // Restaurant's name
    private String address;     // Restaurant's address
    private String cuisineType; // Type of cuisine (Italian, Mexican, etc.)

    private Double averagePricePerPerson; 
    // Average price per person, useful for the budget feature

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public Double getAveragePricePerPerson() {
        return averagePricePerPerson;
    }

    public void setAveragePricePerPerson(Double averagePricePerPerson) {
        this.averagePricePerPerson = averagePricePerPerson;
    }
}
