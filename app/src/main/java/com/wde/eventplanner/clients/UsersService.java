package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.user.RegistrationRequest;
import com.wde.eventplanner.models.user.Token;
import com.wde.eventplanner.models.user.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UsersService {
    @POST("auth/login")
    Call<Token> login(@Body User user);

    @POST("profiles/registration")
    Call<String> register(@Body RegistrationRequest registrationRequest);

    @Multipart
    @PUT("profiles/images")
    Call<String> putImage(@Part MultipartBody.Part imageFile, @Part("profileId") RequestBody profileId);
}
