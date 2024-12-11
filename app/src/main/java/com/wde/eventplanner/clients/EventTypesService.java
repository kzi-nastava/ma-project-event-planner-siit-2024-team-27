package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.EventType;
import com.wde.eventplanner.models.event.RecommendedListingCategoriesDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EventTypesService {
    @GET("event-types")
    Call<ArrayList<EventType>> getEventTypes();

    @GET("event-types/recommended/{id}")
    Call<RecommendedListingCategoriesDTO> getRecommendedListingCategoriesForEventType(@Path("id") String id);
}
