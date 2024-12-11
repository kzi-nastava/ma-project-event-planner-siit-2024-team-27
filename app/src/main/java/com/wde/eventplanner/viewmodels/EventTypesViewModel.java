package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.EventType;
import com.wde.eventplanner.models.event.RecommendedListingCategoriesDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<EventType>> eventTypes = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<EventType>> getEventTypes() {
        return eventTypes;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void fetchEventTypes() {
        ClientUtils.eventTypesService.getEventTypes().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<EventType>> call, @NonNull Response<ArrayList<EventType>> response) {
                if (response.isSuccessful()) {
                    eventTypes.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch event types. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<EventType>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public LiveData<RecommendedListingCategoriesDTO> fetchRecommendedListingCategoriesForEventType(String id) {
        MutableLiveData<RecommendedListingCategoriesDTO> data = new MutableLiveData<>();

        ClientUtils.eventTypesService.getRecommendedListingCategoriesForEventType(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RecommendedListingCategoriesDTO> call, @NonNull Response<RecommendedListingCategoriesDTO> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch event types. Code: " + response.code());
                }
            }
            
            @Override
            public void onFailure(@NonNull Call<RecommendedListingCategoriesDTO> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });

        return data;    
  }
  
  public void editEventType(String id, EventType eventType) {
        ClientUtils.eventTypesService.updateEventType(id, eventType).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EventType> call, @NonNull Response<EventType> response) {
                if (response.isSuccessful()) {
                    ArrayList<EventType> eventTypesNew = eventTypes.getValue();
                    if (eventTypesNew != null) {
                        eventTypesNew.replaceAll(event -> event.getId().equals(id) ? response.body() : event);
                        eventTypes.setValue(new ArrayList<>(eventTypesNew));
                    }
                } else {
                    errorMessage.postValue("Failed to edit event type. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventType> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void createEventType(EventType eventType) {
        ClientUtils.eventTypesService.createEventType(eventType).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EventType> call, @NonNull Response<EventType> response) {
                if (response.isSuccessful()) {
                    ArrayList<EventType> eventTypesNew = eventTypes.getValue();
                    if (eventTypesNew != null) {
                        eventTypesNew.add(response.body());
                        eventTypes.setValue(new ArrayList<>(eventTypesNew));
                    }
                } else {
                    errorMessage.postValue("Failed to create event type. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventType> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
