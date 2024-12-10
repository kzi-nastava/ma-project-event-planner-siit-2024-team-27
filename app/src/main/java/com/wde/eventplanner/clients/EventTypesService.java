package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.EventType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventTypesService {
    @GET("event-types")
    Call<ArrayList<EventType>> getEventTypes();

    @POST("event-types")
    Call<EventType> createEventType(@Body EventType eventType);

    @PUT("event-types/{id}")
    Call<EventType> updateEventType(@Path("id") String id, @Body EventType eventType);
}
