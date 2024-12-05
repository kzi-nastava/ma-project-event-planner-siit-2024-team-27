package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.Event;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventsService {
    @GET("events/top")
    Call<ArrayList<Event>> getTopEvents();

}
