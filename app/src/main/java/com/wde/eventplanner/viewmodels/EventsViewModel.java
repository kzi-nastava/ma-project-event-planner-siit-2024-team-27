package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.event.Event;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Event>> topEventsLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Event>> eventsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<Event>> getTopEvents() {
        return topEventsLiveData;
    }

    public LiveData<ArrayList<Event>> getEvents() {
        return eventsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void fetchTopEvents() {
        ClientUtils.eventsService.getTopEvents().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Event>> call, @NonNull Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    topEventsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch events. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Event>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void fetchEvents() {
        fetchEvents(null, null, null, null, null, null, null, null, null, null, null);
    }

    public void fetchEvents(String searchTerms, String city, String category, String dateRangeStart, String dateRangeEnd,
                            String minRating, String maxRating, String sortBy, String order, String page, String size) {
        ClientUtils.eventsService.getEvents(searchTerms, city, category, dateRangeStart,
                dateRangeEnd, minRating, maxRating, sortBy, order, page, size).enqueue(new Callback<>() {
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
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }
}
