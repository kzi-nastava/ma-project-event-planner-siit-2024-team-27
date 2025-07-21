package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.event.CalendarEvent;
import com.wde.eventplanner.models.event.Event;
import com.wde.eventplanner.models.event.JoinEventDTO;
import com.wde.eventplanner.models.listing.FavoritesRequestDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GuestService {
    @PUT("guests/join-event")
    Call<Void> joinEvent(@Body JoinEventDTO joinEventDTO);

    @GET("guests/{id}/favorite-events")
    Call<ArrayList<Event>> getFavouriteEvents(@Path("id") UUID guestId);

    @PUT("guests/favorite-events")
    Call<Void> setEventFavourite(@Body FavoritesRequestDTO favoritesRequestDTO);

    @GET("guests/{id}/calendar")
    Call<ArrayList<CalendarEvent>> getCalendar(@Path("id") UUID guestId);
}
