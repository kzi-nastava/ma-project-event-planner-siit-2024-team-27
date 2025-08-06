package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.notification.Notification;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationsService {
    @GET("notifications")
    Call<ArrayList<Notification>> getNotifications();

    @PUT("notifications/{id}")
    Call<Void> readNotification(@Path("id") UUID id);
}
