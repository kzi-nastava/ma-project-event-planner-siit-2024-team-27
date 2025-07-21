package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.event.CalendarEvent;
import com.wde.eventplanner.models.listing.FavoritesRequestDTO;
import com.wde.eventplanner.models.listing.Listing;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventOrganizerService {
    @GET("event-organizers/{id}/favorite-listings")
    Call<ArrayList<Listing>> getFavouriteListings(@Path("id") UUID organizerId);

    @GET("event-organizers/{id}/favorite-listings/{isProduct}/{listingId}")
    Call<Boolean> isListingFavourited(@Path("id") UUID organizerId, @Path("isProduct") Boolean isProduct, @Path("listingId") String listingId);

    @PUT("event-organizers/favorite-listings")
    Call<Void> setListingFavourite(@Body FavoritesRequestDTO favoritesRequestDTO);

    @GET("event-organizers/{id}/calendar")
    Call<ArrayList<CalendarEvent>> getCalendar(@Path("id") UUID organizerId);
}
