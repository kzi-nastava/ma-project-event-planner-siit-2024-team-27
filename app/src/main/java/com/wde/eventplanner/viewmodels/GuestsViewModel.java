package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.event.JoinEventDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestsViewModel extends ViewModel {
    public LiveData<String> joinEvent(JoinEventDTO joinEventDTO) {
        MutableLiveData<String> responseMessage = new MutableLiveData<>();

        ClientUtils.guestService.joinEvent(joinEventDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                responseMessage.postValue(response.isSuccessful() ? "ok" : "error");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                responseMessage.postValue("error");
            }
        });

        return responseMessage;
    }
}
