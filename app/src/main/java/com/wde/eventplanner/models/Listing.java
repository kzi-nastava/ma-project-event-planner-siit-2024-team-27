package com.wde.eventplanner.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Listing {
    private String name;
    private ListingType type;
    private String description;
    private Double price;
    private Double oldPrice;
    private Double rating;
    private List<String> images;
}
