package com.wde.eventplanner.models.event;

import com.wde.eventplanner.models.listingCategory.ListingCategory;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecommendedListingCategoriesDTO {
    private List<ListingCategory> serviceCategories;
    private List<ListingCategory> productCategories;
}
