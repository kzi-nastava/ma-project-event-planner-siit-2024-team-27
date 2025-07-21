package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.event.CalendarEvent;
import com.wde.eventplanner.models.listing.FavoritesRequestDTO;
import com.wde.eventplanner.models.listing.Listing;
import com.wde.eventplanner.models.listing.ListingType;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventOrganizerViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public LiveData<ArrayList<Listing>> getFavouriteListings(UUID organizerId) {
        MutableLiveData<ArrayList<Listing>> listings = new MutableLiveData<>();
        ClientUtils.eventOrganizerService.getFavouriteListings(organizerId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Listing>> call, @NonNull Response<ArrayList<Listing>> response) {
                if (response.isSuccessful()) {
                    listings.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch favourite listing. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Listing>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return listings;
    }

    public LiveData<Boolean> isListingFavourited(UUID organizerId, ListingType listingType, String listingId) {
        MutableLiveData<Boolean> isFavourite = new MutableLiveData<>();
        ClientUtils.eventOrganizerService.isListingFavourited(organizerId, listingType == ListingType.PRODUCT, listingId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful()) {
                    isFavourite.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to check favourite listing. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return isFavourite;
    }

    public LiveData<Void> setListingFavourite(UUID organizerId, ListingType listingType, String listingId) {
        MutableLiveData<Void> done = new MutableLiveData<>();
        ClientUtils.eventOrganizerService.setListingFavourite(new FavoritesRequestDTO(organizerId, listingId, listingType)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    done.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to set listing favourite status. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return done;
    }

    public LiveData<ArrayList<CalendarEvent>> getCalendar(UUID organizerId) {
        MutableLiveData<ArrayList<CalendarEvent>> calendar = new MutableLiveData<>();
        ClientUtils.eventOrganizerService.getCalendar(organizerId).enqueue(new Callback<>() {
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
