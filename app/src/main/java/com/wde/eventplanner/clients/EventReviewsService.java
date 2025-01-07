package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.reviews.EventReviewResponse;
import com.wde.eventplanner.models.reviews.ReviewHandling;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface EventReviewsService {
    @GET("event-reviews/pending")
    Call<ArrayList<EventReviewResponse>> getPendingReviews();

    @PUT("event-reviews/process")
    Call<Void> processReview(@Body ReviewHandling reviewHandling);
}
