package com.wde.eventplanner.models.event;

import com.wde.eventplanner.models.listing.ListingType;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ListingBudgetItemDTO {
    private String listingCategoryId;
    private Double maxPrice;
    private String versionedListingId;
    private ListingType listingType;
}
