package com.myapp.controller;

import java.util.List;

public class RestaurantRegistrationRequest {
    private String name;
    private String address;
    private String cuisineType;
    private String phone;
    private String website;
    private String openingHours;
    private Double averagePricePerPerson;
    private Double latitude;
    private Double longitude;
    private List<MenuItemRequest> menuItems;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

    public Double getAveragePricePerPerson() { return averagePricePerPerson; }
    public void setAveragePricePerPerson(Double averagePricePerPerson) { this.averagePricePerPerson = averagePricePerPerson; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public List<MenuItemRequest> getMenuItems() { return menuItems; }
    public void setMenuItems(List<MenuItemRequest> menuItems) { this.menuItems = menuItems; }

    public static class MenuItemRequest {
        private String name;
        private String description;
        private Double price;
        private String category;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }
}