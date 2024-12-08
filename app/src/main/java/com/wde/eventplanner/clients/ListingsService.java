package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.listing.Listing;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ListingsService {
    @GET("listings/top")
    Call<ArrayList<Listing>> getTopListings();

    @GET("listings")
    Call<ArrayList<Listing>> getListings(@Query("searchTerms") String searchTerms,
                                       @Query("type") String type,
                                       @Query("category") String category,
                                       @Query("minPrice") String minPrice,
                                       @Query("maxPrice") String maxPrice,
                                       @Query("minRating") String minRating,
                                       @Query("maxRating") String maxRating,
                                       @Query("sort") String sortBy,
                                       @Query("order") String order,
                                       @Query("page") String page,
                                       @Query("size") String size);
}
