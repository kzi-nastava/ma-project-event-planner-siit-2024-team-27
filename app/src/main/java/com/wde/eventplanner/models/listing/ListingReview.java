package com.wde.eventplanner.models.listing;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ListingReview {
    private int grade;
    private String comment;
    private ListingType listingType;
    private UUID listingId;
    private UUID eventOrganizerId;
}
