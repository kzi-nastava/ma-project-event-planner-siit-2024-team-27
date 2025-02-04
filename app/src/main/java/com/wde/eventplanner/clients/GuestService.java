package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.event.JoinEventDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface GuestService {
    @PUT("guests/join-event")
    Call<Void> joinEvent(@Body JoinEventDTO joinEventDTO);
}
