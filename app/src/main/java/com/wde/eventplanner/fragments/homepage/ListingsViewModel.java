package com.wde.eventplanner.fragments.homepage;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.Listing;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Listing>> listingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<Listing>> getListings() {
        return listingsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchListings() {
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
}
