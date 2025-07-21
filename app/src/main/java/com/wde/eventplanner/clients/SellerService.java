package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.event.CalendarEvent;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SellerService {
    @GET("sellers/{id}/calendar")
    Call<ArrayList<CalendarEvent>> getCalendar(@Path("id") UUID sellerId);
}
