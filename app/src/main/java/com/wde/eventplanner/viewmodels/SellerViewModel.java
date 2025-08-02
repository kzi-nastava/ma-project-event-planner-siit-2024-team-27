package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.event.CalendarEvent;
import com.wde.eventplanner.models.user.Seller;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public LiveData<ArrayList<CalendarEvent>> getCalendar(UUID sellerId) {
        MutableLiveData<ArrayList<CalendarEvent>> calendar = new MutableLiveData<>();
        ClientUtils.sellerService.getCalendar(sellerId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CalendarEvent>> call, @NonNull Response<ArrayList<CalendarEvent>> response) {
                if (response.isSuccessful()) {
                    calendar.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch calendar. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CalendarEvent>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return calendar;
    }

    public LiveData<Seller> getSeller(UUID sellerId) {
        MutableLiveData<Seller> seller = new MutableLiveData<>();
        ClientUtils.sellerService.getSeller(sellerId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Seller> call, @NonNull Response<Seller> response) {
                if (response.isSuccessful()) {
                    seller.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch seller. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Seller> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return seller;
    }
}
