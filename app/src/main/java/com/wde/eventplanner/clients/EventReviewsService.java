package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.event.EventReview;
import com.wde.eventplanner.models.reviews.EventReviewResponse;
import com.wde.eventplanner.models.reviews.ReviewDistribution;
import com.wde.eventplanner.models.reviews.ReviewHandling;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventReviewsService {
    @GET("event-reviews/pending")
    Call<ArrayList<EventReviewResponse>> getPendingReviews();

    @PUT("event-reviews/process")
    Call<Void> processReview(@Body ReviewHandling reviewHandling);

    @GET("event-reviews/distribution/{eventId}")
    Call<ReviewDistribution> getReviewDistribution(@Path("eventId") UUID eventId);

    @POST("event-reviews")
    Call<Void> createReview(@Body EventReview review);

    @GET("event-reviews/check/{guestId}/{eventId}")
    Call<Void> checkIfAllowed(@Path("guestId") UUID guestId, @Path("eventId") UUID eventId);

    @GET("event-reviews/event/{eventId}")
    Call<ArrayList<EventReviewResponse>> getReviews(@Path("eventId") UUID eventId);
}
