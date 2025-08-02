package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.reports.UserReport;
import com.wde.eventplanner.models.reports.UserReportResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserReportsService {
    @GET("user-reports")
    Call<ArrayList<UserReportResponse>> getReports();

    @POST("user-reports")
    Call<Void> createReport(@Body UserReport userReport);

    @PUT("user-reports")
    Call<Void> processReport(@Body UserReport userReport);
}
