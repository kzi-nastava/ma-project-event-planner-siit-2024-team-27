package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.services.EditServiceDTO;
import com.wde.eventplanner.models.services.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public LiveData<Service> getService(String id) {
        MutableLiveData<Service> service = new MutableLiveData<>();
        ClientUtils.servicesService.getServiceLatestVersion(UUID.fromString(id)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Service> call, @NonNull Response<Service> response) {
                if (response.isSuccessful()) {
                    service.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch service. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Service> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return service;
    }

    public LiveData<EditServiceDTO> getEditService(String id) {
        MutableLiveData<EditServiceDTO> service = new MutableLiveData<>();
        ClientUtils.servicesService.getEditService(UUID.fromString(id)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EditServiceDTO> call, @NonNull Response<EditServiceDTO> response) {
                if (response.isSuccessful()) {
                    service.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch service. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EditServiceDTO> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return service;
    }

    private static void put(Map<String, RequestBody> map, String key, Object value) {
        if (value != null) map.put(key, toRB(value.toString()));
    }

    private static RequestBody toRB(String value) {
        return RequestBody.create(value, MediaType.parse("text/plain"));
    }

    public LiveData<Service> createService(List<File> images, UUID sellerId, String name, Boolean isAvailable, Double price, Double salePercentage, String serviceCategoryId,
                                           Integer reservationDeadline, Integer cancellationDeadline, Boolean isConfirmationManual, Boolean isPrivate,
                                           Integer minimumDuration, Integer maximumDuration, String description, String suggestedCategory,
                                           String suggestedCategoryDescription, List<String> availableEventTypeIds) {
        MutableLiveData<Service> service = new MutableLiveData<>();
        List<MultipartBody.Part> fileParts = new ArrayList<>();
        Map<String, RequestBody> fields = new HashMap<>();

        if (images != null)
            for (File file : images)
                fileParts.add(MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(file, MediaType.parse("image/*"))));

        put(fields, "sellerId", sellerId);
        put(fields, "name", name);
        put(fields, "isAvailable", isAvailable);
        put(fields, "price", price);
        put(fields, "salePercentage", salePercentage);
        put(fields, "serviceCategoryId", serviceCategoryId);
        put(fields, "reservationDeadline", reservationDeadline);
        put(fields, "cancellationDeadline", cancellationDeadline);
        put(fields, "isConfirmationManual", isConfirmationManual);
        put(fields, "isPrivate", isPrivate);
        put(fields, "minimumDuration", minimumDuration);
        put(fields, "maximumDuration", maximumDuration);
        put(fields, "description", description);
        put(fields, "suggestedCategory", suggestedCategory);
        put(fields, "suggestedCategoryDescription", suggestedCategoryDescription);

        if (availableEventTypeIds != null)
            for (int i = 0; i < availableEventTypeIds.size(); i++)
                fields.put("availableEventTypeIds[" + i + "]", toRB(availableEventTypeIds.get(i)));

        ClientUtils.servicesService.createService(fileParts, fields).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Service> call, @NonNull Response<Service> response) {
                if (response.isSuccessful()) {
                    service.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to create service. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Service> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });

        return service;
    }

    public LiveData<Service> updateService(List<File> images, String staticServiceId, String name, Boolean isAvailable, Double price, Double salePercentage,
                                           Integer reservationDeadline, Integer cancellationDeadline, Boolean isConfirmationManual, Boolean isPrivate,
                                           Integer minimumDuration, Integer maximumDuration, String description, List<String> availableEventTypeIds) {
        MutableLiveData<Service> service = new MutableLiveData<>();
        List<MultipartBody.Part> fileParts = new ArrayList<>();
        Map<String, RequestBody> fields = new HashMap<>();

        if (images != null)
            for (File file : images)
                fileParts.add(MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(file, MediaType.parse("image/*"))));

        put(fields, "staticServiceId", staticServiceId);
        put(fields, "name", name);
        put(fields, "isAvailable", isAvailable);
        put(fields, "price", price);
        put(fields, "salePercentage", salePercentage);
        put(fields, "reservationDeadline", reservationDeadline);
        put(fields, "cancellationDeadline", cancellationDeadline);
        put(fields, "isConfirmationManual", isConfirmationManual);
        put(fields, "isPrivate", isPrivate);
        put(fields, "minimumDuration", minimumDuration);
        put(fields, "maximumDuration", maximumDuration);
        put(fields, "description", description);

        if (availableEventTypeIds != null)
            for (int i = 0; i < availableEventTypeIds.size(); i++)
                fields.put("availableEventTypeIds[" + i + "]", toRB(availableEventTypeIds.get(i)));

        ClientUtils.servicesService.updateService(fileParts, fields).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Service> call, @NonNull Response<Service> response) {
                if (response.isSuccessful()) {
                    service.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to update service. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Service> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });

        return service;
    }

    public LiveData<Void> deleteService(String id) {
        MutableLiveData<Void> done = new MutableLiveData<>();
        ClientUtils.servicesService.deleteService(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    done.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to delete service. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return done;
    }
}
