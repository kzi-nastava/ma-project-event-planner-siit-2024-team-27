package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.event.EventReview;
import com.wde.eventplanner.models.reviews.EventReviewResponse;
import com.wde.eventplanner.models.reviews.ReviewDistribution;
import com.wde.eventplanner.models.reviews.ReviewHandling;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventReviewsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<EventReviewResponse>> pendingReviews = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<EventReviewResponse>> getPendingReviews() {
        return pendingReviews;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void fetchPendingReviews() {
        ClientUtils.eventReviewsService.getPendingReviews().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<EventReviewResponse>> call, @NonNull Response<ArrayList<EventReviewResponse>> response) {
                if (response.isSuccessful()) {
                    pendingReviews.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch pending event reviews. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<EventReviewResponse>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public LiveData<ReviewDistribution> getReviewDistribution(UUID eventId) {
        MutableLiveData<ReviewDistribution> reviewDistribution = new MutableLiveData<>();
        ClientUtils.eventReviewsService.getReviewDistribution(eventId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ReviewDistribution> call, @NonNull Response<ReviewDistribution> response) {
                if (response.isSuccessful()) {
                    reviewDistribution.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch review distribution. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewDistribution> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return reviewDistribution;
    }

    public void processReview(ReviewHandling reviewHandling) {
        ClientUtils.eventReviewsService.processReview(reviewHandling).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    ArrayList<EventReviewResponse> reviews = pendingReviews.getValue();
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

    public LiveData<String> createReview(EventReview eventReview) {
        MutableLiveData<String> done = new MutableLiveData<>();
        ClientUtils.eventReviewsService.createReview(eventReview).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    done.postValue("Successfully created review!");
                } else {
                    done.postValue("Failed to create a review. Code: " + response.code());
                    errorMessage.postValue("Failed to create a review. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return done;
    }

    public LiveData<Boolean> checkIfAllowed(UUID guestId, UUID eventId) {
        MutableLiveData<Boolean> isAllowed = new MutableLiveData<>();
        ClientUtils.eventReviewsService.checkIfAllowed(guestId, eventId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    isAllowed.postValue(true);
                } else {
                    isAllowed.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return isAllowed;
    }

    public LiveData<ArrayList<EventReviewResponse>> getReviews(UUID eventId) {
        MutableLiveData<ArrayList<EventReviewResponse>> reviews = new MutableLiveData<>();
        ClientUtils.eventReviewsService.getReviews(eventId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<EventReviewResponse>> call, @NonNull Response<ArrayList<EventReviewResponse>> response) {
                if (response.isSuccessful()) {
                    reviews.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch event reviews. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<EventReviewResponse>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return reviews;
    }
}
