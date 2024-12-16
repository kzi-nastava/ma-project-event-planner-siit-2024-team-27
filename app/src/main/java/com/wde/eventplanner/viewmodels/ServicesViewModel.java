package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.services.Service;
import com.wde.eventplanner.models.services.Service;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesViewModel extends ViewModel {
    private final MutableLiveData<Service> serviceLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<Service> getService() { return this.serviceLiveData; }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void fetchService(String id) {
        ClientUtils.servicesService.getServiceLatestVersion(UUID.fromString(id)).enqueue(new Callback<Service>() {
            @Override
            public void onResponse(@NonNull Call<Service> call, @NonNull Response<Service> response) {
                if (response.isSuccessful()) {
                    serviceLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch service. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Service> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }
}
