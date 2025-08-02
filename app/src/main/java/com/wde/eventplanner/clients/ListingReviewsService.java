package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.listing.ListingReview;
import com.wde.eventplanner.models.reviews.ListingReviewResponse;
import com.wde.eventplanner.models.reviews.ReviewHandling;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ListingReviewsService {
    @GET("listing-reviews/pending")
    Call<ArrayList<ListingReviewResponse>> getPendingReviews();

    @PUT("listing-reviews")
    Call<Void> processReview(@Body ReviewHandling reviewHandling);

    @POST("listing-reviews")
    Call<Void> createReview(@Body ListingReview review);

    @GET("listing-reviews/check/{organizerId}/{isProduct}/{listingId}")
    Call<Void> checkIfAllowed(@Path("organizerId") UUID organizerId, @Path("isProduct") Boolean isProduct, @Path("listingId") UUID listingId);

    @GET("listing-reviews/listing/{listingId}/{isProduct}")
    Call<ArrayList<ListingReviewResponse>> getReviews(@Path("listingId") UUID listingId, @Path("isProduct") Boolean isProduct);
}
