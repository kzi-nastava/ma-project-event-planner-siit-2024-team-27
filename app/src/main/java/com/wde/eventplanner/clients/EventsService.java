package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.Page;
import com.wde.eventplanner.models.event.AgendaItem;
import com.wde.eventplanner.models.event.CreateEventDTO;
import com.wde.eventplanner.models.event.Event;
import com.wde.eventplanner.models.event.EventAdminDTO;
import com.wde.eventplanner.models.event.EventComplexView;
import com.wde.eventplanner.models.event.EventActivitiesDTO;
import com.wde.eventplanner.models.event.EventDetailedDTO;

import java.util.ArrayList;
import java.util.UUID;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
                                @Query("size") String size,
                                @Query("organizerId") String organizerId);

    @DELETE("events/{id}/{userId}")
    Call<String> deleteEvent(@Path("id") UUID id, @Path("userId") UUID userId);

    @GET("events/{id}/my-events")
    Call<ArrayList<EventComplexView>> getEventsFromOrganizer(@Path("id") UUID id);

    @GET("events/{organizerId}/my-events/{eventId}")
    Call<EventComplexView> getEventFromOrganizer(@Path("organizerId") UUID organizerId, @Path("eventId") UUID eventId);

    @POST("events")
    Call<Event> createEvent(@Body CreateEventDTO createEventDTO);

    @PUT("events")
    Call<Void> updateEvent(@Body CreateEventDTO createEventDTO);

    @PUT("events/images")
    Call<Void> putImages(@Body RequestBody body);

    @GET("events/agenda/{id}")
    Call<ArrayList<AgendaItem>> getAgenda(@Path("id") UUID id);

    @POST("events/agenda")
    Call<ArrayList<UUID>> createAgenda(@Body EventActivitiesDTO eventActivitiesDTO);

    @PUT("events/agenda")
    Call<Void> updateAgenda(@Body EventActivitiesDTO eventActivitiesDTO);

    @GET("events/public")
    Call<ArrayList<EventAdminDTO>> getPublicEvents();

    @GET("events/{id}/pdf")
    Call<ResponseBody> getPdfReport(@Path("id") UUID id);

    @GET("events/{id}/detailed-view/{isGuest}/{userId}")
    Call<EventDetailedDTO> getEvent(@Path("id") UUID id, @Path("isGuest") boolean isGuest, @Path("userId") UUID userId);
}
