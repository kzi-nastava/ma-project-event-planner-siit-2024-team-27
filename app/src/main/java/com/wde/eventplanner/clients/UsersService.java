package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.user.Profile;
import com.wde.eventplanner.models.user.Token;
import com.wde.eventplanner.models.user.User;
import com.wde.eventplanner.models.user.UserBlock;

import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UsersService {
    @POST("auth/login")
    Call<Token> login(@Body User user);

    @POST("profiles/registration")
    Call<String> register(@Body Profile profile);

    @Multipart
    @PUT("profiles/images")
    Call<String> putImage(@Part MultipartBody.Part imageFile, @Part("profileId") RequestBody profileId);

    @GET("profiles/{id}")
    Call<Profile> get(@Path("id") UUID id);

    @PUT("profiles/{id}")
    Call<Profile> update(@Path("id") UUID id, @Body Profile profile);

    @DELETE("profiles/{id}")
    Call<Void> delete(@Path("id") UUID id);

    @PUT("profiles/blocking")
    Call<Void> block(@Body UserBlock userBlock);
}
