package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.products.Product;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductsService {
    @GET("products/{id}/latest-version")
    Call<Product> getProductLatestVersion(@Path("id") UUID id);
}
