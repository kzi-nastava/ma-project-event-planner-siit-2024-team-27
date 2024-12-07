package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.models.Notification;
import com.wde.eventplanner.clients.ClientUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Notification>> notificationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<Notification>> getNotifications() {
        return notificationsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchNotifications() {
        ClientUtils.notificationsService.getNotifications().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Notification>> call, @NonNull Response<ArrayList<Notification>> response) {
                if (response.isSuccessful()) {
                    notificationsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch notifications. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Notification>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void readNotification(String id) {
        ClientUtils.notificationsService.readNotification(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful())
                    errorMessage.postValue("Failed to read notifications. Code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }
}
