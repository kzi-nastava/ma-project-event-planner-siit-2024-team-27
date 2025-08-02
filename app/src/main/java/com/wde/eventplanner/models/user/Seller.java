package com.wde.eventplanner.models.user;

import com.wde.eventplanner.models.reviews.ListingReviewResponse;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seller {
    private UUID sellerId;
    private String name;
    private String surname;
    private String address;
    private String telephoneNumber;
    private String city;
    private String description;
    private String email;
    private List<ListingReviewResponse> reviews;
    private Double rating;
    private String image;
    private UUID sellerProfileId;
}
