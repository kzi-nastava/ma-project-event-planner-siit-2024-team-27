package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.services.EditServiceDTO;
import com.wde.eventplanner.models.services.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ServicesService {
    @GET("services/{id}/latest-version")
    Call<Service> getServiceLatestVersion(@Path("id") UUID id);

    @GET("services/{id}/latest-version/edit")
    Call<EditServiceDTO> getEditService(@Path("id") UUID id);

    @Multipart
    @POST("services")
    Call<Service> createService(@Part List<MultipartBody.Part> images, @PartMap Map<String, RequestBody> fields);

    @Multipart
    @PUT("services")
    Call<Service> updateService(@Part List<MultipartBody.Part> images, @PartMap Map<String, RequestBody> fields);

    @DELETE("services/{id}")
    Call<Void> deleteService(@Path("id") String id);
}
