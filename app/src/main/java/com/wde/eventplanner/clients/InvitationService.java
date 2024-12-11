package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.event.GuestInfo;
import com.wde.eventplanner.models.event.GuestList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InvitationService {
    @GET("guests/email/{email}")
    Call<GuestInfo> getGuestInfo(@Path("email") String email);

    @POST("invitations")
    Call<Void> sendInvitations(@Body GuestList guestList);
}
