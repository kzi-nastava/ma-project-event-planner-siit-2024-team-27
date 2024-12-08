package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategoryDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ListingCategoriesService {
    @GET("listing-categories/pending")
    Call<ArrayList<ListingCategoryDTO>> getPendingCategories();

    @POST("listing-categories/pending")
    Call<ListingCategoryDTO> createPendingListingCategory(@Body ListingCategoryDTO listingCategoryDTO);

    @PUT("listing-categories/pending/replace")
    Call<Void> replacePendingListingCategory(@Body ReplacingListingCategoryDTO replacingListingCategoryDTO);

    @PUT("listing-categories/pending/{id}")
    Call<ListingCategoryDTO> approvePendingListingCategory(@Path("id") String id);

    @GET("listing-categories")
    Call<ArrayList<ListingCategoryDTO>> getCategories();

    @POST("listing-categories")
    Call<ListingCategoryDTO> createActiveListingCategory(@Body ListingCategoryDTO listingCategoryDTO);

    @PUT("listing-categories/{id}")
    Call<ListingCategoryDTO> updateListingCategory(@Path("id") String id, @Body ListingCategoryDTO listingCategoryDTO);

    @DELETE("listing-categories/{id}")
    Call<String> deleteListingCategory(@Path("id") String id);
}
