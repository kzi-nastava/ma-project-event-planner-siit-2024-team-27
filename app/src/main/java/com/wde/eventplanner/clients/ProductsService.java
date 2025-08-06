package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.products.CatalogueProduct;
import com.wde.eventplanner.models.products.EditProductDTO;
import com.wde.eventplanner.models.products.Product;
import com.wde.eventplanner.models.products.UpdateCatalogueProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ProductsService {
    @GET("products/{id}/latest-version")
    Call<Product> getProductLatestVersion(@Path("id") UUID id);

    @GET("products/{id}/latest-version/edit")
    Call<EditProductDTO> getEditProduct(@Path("id") UUID id);

    @Multipart
    @POST("products")
    Call<Product> createProduct(@Part List<MultipartBody.Part> images, @PartMap Map<String, RequestBody> fields);

    @Multipart
    @PUT("products")
    Call<Product> updateProduct(@Part List<MultipartBody.Part> images, @PartMap Map<String, RequestBody> fields);

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") String id);

    @GET("products/{id}/my-products/catalogue")
    Call<ArrayList<CatalogueProduct>> getCatalogue(@Path("id") String id);

    @POST("products/{id}/update-catalogue")
    Call<Void> updateCatalogue(@Path("id") String id, @Body UpdateCatalogueProduct catalogue);

    @GET("products/{id}/catalogue-pdf")
    Call<ResponseBody> getPdfCatalogue(@Path("id") String id);
}
