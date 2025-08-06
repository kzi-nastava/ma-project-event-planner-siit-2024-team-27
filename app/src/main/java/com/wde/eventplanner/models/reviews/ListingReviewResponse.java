package com.wde.eventplanner.models.reviews;

import com.wde.eventplanner.models.listing.ListingType;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ListingReviewResponse {
    private UUID id;
    private int grade;
    private String comment;
    private ListingType listingType;
    private UUID listingId;
    private UUID eventOrganizerId;
    private String guestName;
    private String guestSurname;
}
