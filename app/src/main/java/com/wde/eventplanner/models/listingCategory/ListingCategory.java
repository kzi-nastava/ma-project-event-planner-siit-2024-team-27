package com.wde.eventplanner.models.listingCategory;

import androidx.annotation.NonNull;

import com.wde.eventplanner.models.listing.ListingType;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ListingCategory {
    private String id;

    private String name;
    private Boolean isPending;
    private String description;
    private Boolean isDeleted;
    private ListingType listingType;

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    // admin creating a new category constructor
    public ListingCategory(String name, String description, ListingType listingType) {
        this.name = name;
        this.description = description;
        this.isPending = false;
        this.isDeleted = false;
        this.listingType = listingType;
    }

    // admin editing a category constructor
    public ListingCategory(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
