package com.wde.eventplanner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Listing {
    private String title;
    private String originalPrice;
    private String price;
    private float rating;
}
