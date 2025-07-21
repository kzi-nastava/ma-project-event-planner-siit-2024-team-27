package com.wde.eventplanner.models.listing;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoritesRequestDTO {
    private UUID userId;
    private String favoriteItemId;
    private ListingType listingType;
}

