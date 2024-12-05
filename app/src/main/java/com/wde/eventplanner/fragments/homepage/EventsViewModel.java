package com.wde.eventplanner.fragments.homepage;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.Event;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Event>> eventsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<Event>> getEvents() {
        return eventsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchEvents() {
        ClientUtils.eventsService.getTopEvents().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Event>> call, @NonNull Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    eventsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch events. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Event>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " +t.getMessage());
            }
        });
    }
}
