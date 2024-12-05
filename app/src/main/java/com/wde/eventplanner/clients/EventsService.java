package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.Event;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EventsService {
    @GET("events/top")
    Call<ArrayList<Event>> getTopEvents();

    @GET("events")
    Call<ArrayList<Event>> getEvents(@Query("searchTerms") String searchTerms,
                                     @Query("city") String city,
                                     @Query("type") String category,
                                     @Query("after") String dateRangeStart,
                                     @Query("before") String dateRangeEnd,
                                     @Query("minRating") String minRating,
                                     @Query("maxRating") String maxRating,
                                     @Query("sort") String sortBy,
                                     @Query("order") String order,
                                     @Query("page") String page,
                                     @Query("size") String size);
}
