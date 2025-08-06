package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.reports.UserReport;
import com.wde.eventplanner.models.reports.UserReportResponse;

import java.util.ArrayList;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReportsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<UserReportResponse>> pendingReports = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<UserReportResponse>> getReports() {
        return pendingReports;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void fetchReports() {
        ClientUtils.userReportsService.getReports().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<UserReportResponse>> call, @NonNull Response<ArrayList<UserReportResponse>> response) {
                if (response.isSuccessful()) {
                    pendingReports.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch reports. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<UserReportResponse>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public LiveData<Void> createReport(UserReport userReport) {
        MutableLiveData<Void> done = new MutableLiveData<>();
        ClientUtils.userReportsService.createReport(userReport).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    done.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to create the report. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return done;
    }

    public void processReport(UserReport userReport) {
        ClientUtils.userReportsService.processReport(userReport).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    ArrayList<UserReportResponse> reports = pendingReports.getValue();
                    if (reports != null) {
                        reports = reports.stream().filter(r -> !r.getId().equals(userReport.getId())).collect(Collectors.toCollection(ArrayList::new));
                        pendingReports.postValue(reports);
                    }
                } else {
                    errorMessage.postValue("Failed to process the report. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
