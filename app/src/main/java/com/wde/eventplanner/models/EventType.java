package com.wde.eventplanner.models;

import com.wde.eventplanner.models.listingCategory.ListingCategory;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EventType {
    private String id;
    private String name;
    private String description;
    private Boolean isActive;
    private ArrayList<ListingCategory> listingCategories;
}
