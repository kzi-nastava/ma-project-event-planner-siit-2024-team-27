package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.services.Service;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServicesService {
    @GET("services/{id}/latest-version")
    Call<Service> getServiceLatestVersion(@Path("id") UUID id);
}
