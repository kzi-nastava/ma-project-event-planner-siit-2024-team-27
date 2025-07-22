package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.Page;
import com.wde.eventplanner.models.listing.Listing;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ListingsService {
    @GET("listings/top")
    Call<ArrayList<Listing>> getTopListings();

    @GET("listings")
    Call<Page<Listing>> getListings(@Query("searchTerms") String searchTerms,
                                    @Query("type") String type,
                                    @Query("category") String category,
                                    @Query("minPrice") String minPrice,
                                    @Query("maxPrice") String maxPrice,
                                    @Query("minRating") String minRating,
                                    @Query("maxRating") String maxRating,
                                    @Query("sortBy") String sortBy,
                                    @Query("order") String order,
                                    @Query("page") Integer page,
                                    @Query("size") Integer size);

    @GET("listings/{sellerId}")
    Call<Page<Listing>> getListings(@Path("sellerId") UUID sellerId,
                                    @Query("searchTerms") String searchTerms,
                                    @Query("type") String type,
                                    @Query("category") String category,
                                    @Query("minPrice") String minPrice,
                                    @Query("maxPrice") String maxPrice,
                                    @Query("minRating") String minRating,
                                    @Query("maxRating") String maxRating,
                                    @Query("sortBy") String sortBy,
                                    @Query("order") String order,
                                    @Query("page") Integer page,
                                    @Query("size") Integer size);
}
