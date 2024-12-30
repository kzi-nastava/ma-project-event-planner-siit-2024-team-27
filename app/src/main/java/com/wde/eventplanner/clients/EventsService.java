package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.Page;
import com.wde.eventplanner.models.event.Event;
import com.wde.eventplanner.models.event.EventComplexView;
import com.wde.eventplanner.models.event.EventActivitiesDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventsService {
    @GET("events/top")
    Call<ArrayList<Event>> getTopEvents();

    @GET("events")
    Call<Page<Event>> getEvents(@Query("searchTerms") String searchTerms,
                                @Query("city") String city,
                                @Query("type") String category,
                                @Query("after") String dateRangeStart,
                                @Query("before") String dateRangeEnd,
                                @Query("minRating") String minRating,
                                @Query("maxRating") String maxRating,
                                @Query("sortBy") String sortBy,
                                @Query("order") String order,
                                @Query("page") String page,
                                @Query("size") String size);

    @GET("events/{id}/my-events")
    Call<ArrayList<EventComplexView>> getEventsFromOrganizer(@Path("id") UUID id);

    @POST("events/agenda")
    Call<ArrayList<UUID>> createAgenda(@Body EventActivitiesDTO eventActivitiesDTO);
}
