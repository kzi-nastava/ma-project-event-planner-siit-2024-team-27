package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.reviews.ListingReviewResponse;
import com.wde.eventplanner.models.reviews.ReviewHandling;

import java.util.ArrayList;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingReviewsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ListingReviewResponse>> pendingReviews = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<ListingReviewResponse>> getPendingReviews() {
        return pendingReviews;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void fetchPendingReviews() {
        ClientUtils.listingReviewsService.getPendingReviews().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ListingReviewResponse>> call, @NonNull Response<ArrayList<ListingReviewResponse>> response) {
                if (response.isSuccessful()) {
                    pendingReviews.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch pending listing reviews. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ListingReviewResponse>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void processReview(ReviewHandling reviewHandling) {
        ClientUtils.listingReviewsService.processReview(reviewHandling).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingReviewResponse> reviews = pendingReviews.getValue();
                    if (reviews != null) {
                        reviews = reviews.stream().filter(c -> !c.getId().equals(reviewHandling.getId())).collect(Collectors.toCollection(ArrayList::new));
                        pendingReviews.postValue(reviews);
                    }
                } else
                    errorMessage.postValue("Failed to process review. Code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
