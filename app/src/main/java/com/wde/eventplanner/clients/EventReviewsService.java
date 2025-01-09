package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.reviews.EventReviewResponse;
import com.wde.eventplanner.models.reviews.ReviewDistribution;
import com.wde.eventplanner.models.reviews.ReviewHandling;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventReviewsService {
    @GET("event-reviews/pending")
    Call<ArrayList<EventReviewResponse>> getPendingReviews();

    @PUT("event-reviews/process")
    Call<Void> processReview(@Body ReviewHandling reviewHandling);

    @GET("event-reviews/distribution/{eventId}")
    Call<ReviewDistribution> getReviewDistribution(@Path("eventId") UUID eventId);
}
