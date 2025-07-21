package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.event.CalendarEvent;
import com.wde.eventplanner.models.event.Event;
import com.wde.eventplanner.models.event.JoinEventDTO;
import com.wde.eventplanner.models.listing.FavoritesRequestDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestsViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public LiveData<ArrayList<Event>> getFavouriteEvents(UUID guestId) {
        MutableLiveData<ArrayList<Event>> events = new MutableLiveData<>();
        ClientUtils.guestService.getFavouriteEvents(guestId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Event>> call, @NonNull Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    events.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch favourite events. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Event>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return events;
    }

    public LiveData<Void> setEventFavourite(UUID organizerId, String eventId) {
        MutableLiveData<Void> done = new MutableLiveData<>();
        ClientUtils.guestService.setEventFavourite(new FavoritesRequestDTO(organizerId, eventId, null)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    done.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to set event favourite status. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return done;
    }

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

    public LiveData<ArrayList<CalendarEvent>> getCalendar(UUID guestId) {
        MutableLiveData<ArrayList<CalendarEvent>> calendar = new MutableLiveData<>();
        ClientUtils.guestService.getCalendar(guestId).enqueue(new Callback<>() {
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
}
