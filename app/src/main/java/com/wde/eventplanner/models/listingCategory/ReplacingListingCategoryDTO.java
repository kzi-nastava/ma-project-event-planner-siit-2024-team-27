package com.wde.eventplanner.models.listingCategory;

import com.wde.eventplanner.models.listing.ListingType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReplacingListingCategoryDTO {
    private String toBeReplacedId;
    private String replacingId;
    private ListingType listingType;
}
