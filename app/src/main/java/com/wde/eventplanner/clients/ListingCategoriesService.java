package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategory;

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
    Call<ArrayList<ListingCategory>> getPendingCategories();

    @POST("listing-categories/pending")
    Call<ListingCategory> createPendingListingCategory(@Body ListingCategory listingCategory);

    @PUT("listing-categories/pending/replace")
    Call<Void> replacePendingListingCategory(@Body ReplacingListingCategory replacingListingCategory);

    @PUT("listing-categories/pending/{id}")
    Call<ListingCategory> approvePendingListingCategory(@Path("id") String id);

    @GET("listing-categories")
    Call<ArrayList<ListingCategory>> getCategories();

    @POST("listing-categories")
    Call<ListingCategory> createActiveListingCategory(@Body ListingCategory listingCategory);

    @PUT("listing-categories/{id}")
    Call<ListingCategory> updateListingCategory(@Path("id") String id, @Body ListingCategory listingCategory);

    @DELETE("listing-categories/{id}")
    Call<String> deleteListingCategory(@Path("id") String id);
}
