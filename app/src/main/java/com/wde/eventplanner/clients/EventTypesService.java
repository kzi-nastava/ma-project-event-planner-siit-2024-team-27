package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.EventType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventTypesService {
    @GET("event-types")
    Call<ArrayList<EventType>> getTypes();
}
