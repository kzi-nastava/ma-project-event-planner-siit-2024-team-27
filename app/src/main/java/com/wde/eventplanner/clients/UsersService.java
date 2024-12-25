package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.user.Token;
import com.wde.eventplanner.models.user.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsersService {
    @POST("auth/login")
    Call<Token> login(@Body User user);
}
