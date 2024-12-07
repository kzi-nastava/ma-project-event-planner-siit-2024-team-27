package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.listing.Listing;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Listing>> listingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public boolean isReady() {
        if (listingsLiveData.getValue() == null) return false;
        return !listingsLiveData.getValue().isEmpty();
    }

    public LiveData<ArrayList<Listing>> getListings() {
        return listingsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchTopListings() {
        ClientUtils.listingsService.getTopListings().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Listing>> call, @NonNull Response<ArrayList<Listing>> response) {
                if (response.isSuccessful()) {
                    listingsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch listings. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Listing>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void fetchListings(String searchTerms, String type, String category, String minPrice, String maxPrice,
                              String minRating, String maxRating, String sortBy, String order, String page, String size) {
        ClientUtils.listingsService.getListings(searchTerms, type, category, minPrice, maxPrice,
                minRating, maxRating, sortBy, order, page, size).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Listing>> call, @NonNull Response<ArrayList<Listing>> response) {
                if (response.isSuccessful()) {
                    listingsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch listings. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Listing>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }
}
