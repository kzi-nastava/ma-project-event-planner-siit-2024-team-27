package com.wde.eventplanner.models;

public class Listing {
    private String title;
    private String originalPrice;
    private String price;
    private float rating;

    public Listing(String title, String originalPrice, String price, float rating) {
        this.title = title;
        this.originalPrice = originalPrice;
        this.price = price;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }
}
