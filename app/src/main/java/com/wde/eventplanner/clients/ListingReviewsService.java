package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.reviews.ListingReviewResponse;
import com.wde.eventplanner.models.reviews.ReviewHandling;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface ListingReviewsService {
    @GET("listing-reviews/pending")
    Call<ArrayList<ListingReviewResponse>> getPendingReviews();

    @PUT("listing-reviews")
    Call<Void> processReview(@Body ReviewHandling reviewHandling);
}
