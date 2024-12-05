package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.Listing;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ListingsService {
    @GET("listings/top")
    Call<ArrayList<Listing>> getTopListings();

}
