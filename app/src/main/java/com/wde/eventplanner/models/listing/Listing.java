package com.wde.eventplanner.models.listing;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Listing {
    private String id;
    private Integer version;
    private String name;
    private ListingType type;
    private String description;
    private Double price;
    private Double oldPrice;
    private Double rating;
    private List<String> images;
}
