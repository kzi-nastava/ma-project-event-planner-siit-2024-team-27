package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.Page;
import com.wde.eventplanner.models.listing.Listing;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Listing>> topListingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Page<Listing>> listingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<Listing>> getTopListings() {
        return topListingsLiveData;
    }

    public LiveData<Page<Listing>> getListings() {
        return listingsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void fetchTopListings() {
        ClientUtils.listingsService.getTopListings().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Listing>> call, @NonNull Response<ArrayList<Listing>> response) {
                if (response.isSuccessful()) {
                    topListingsLiveData.postValue(response.body());
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

    public void fetchListings() {
        fetchListings(null, null, null, null, null, null, null, null, null, null, null);
    }

    public void fetchListings(String searchTerms, String type, String category, String minPrice, String maxPrice,
                              String minRating, String maxRating, String sortBy, String order, Integer page, Integer size) {
        ClientUtils.listingsService.getListings(searchTerms, type, category, minPrice, maxPrice,
                minRating, maxRating, sortBy, order, page, size).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Page<Listing>> call, @NonNull Response<Page<Listing>> response) {
                if (response.isSuccessful()) {
                    listingsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch listings. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Page<Listing>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void fetchListings(UUID sellerId, String searchTerms, String type, String category, String minPrice, String maxPrice,
                              String minRating, String maxRating, String sortBy, String order, Integer page, Integer size) {
        ClientUtils.listingsService.getListings(sellerId, searchTerms, type, category, minPrice, maxPrice,
                minRating, maxRating, sortBy, order, page, size).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Page<Listing>> call, @NonNull Response<Page<Listing>> response) {
                if (response.isSuccessful()) {
                    listingsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch sellers listings. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Page<Listing>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }
}
