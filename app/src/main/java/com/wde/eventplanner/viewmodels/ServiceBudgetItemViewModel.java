package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.serviceBudgetItem.BookingSlots;
import com.wde.eventplanner.models.serviceBudgetItem.ReserveService;
import com.wde.eventplanner.models.serviceBudgetItem.ServiceBudgetItem;
import com.wde.eventplanner.models.services.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceBudgetItemViewModel extends ViewModel {
    private final MutableLiveData<List<BookingSlots>> freeSlotsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<List<BookingSlots>> getFreeSlots() {
        return freeSlotsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public LiveData<Boolean> reserveService(ReserveService reserveService) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();

        ClientUtils.serviceBudgetItemService.reserveService(reserveService).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful())
                    errorMessage.postValue("Failed to reserve service. Code: " + response.code());
                success.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });

        return success;
    }

    public void fetchSlotsForService(Service service) {
        ClientUtils.serviceBudgetItemService.getSlotsForService(service.getStaticServiceId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<BookingSlots>> call, @NonNull Response<List<BookingSlots>> response) {
                if (response.isSuccessful()) {
                    freeSlotsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to get free reservation slots. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BookingSlots>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public LiveData<Boolean> createServiceBudgetItem(ServiceBudgetItem serviceBudgetItem) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();

        ClientUtils.serviceBudgetItemService.createServiceBudgetItem(serviceBudgetItem).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ServiceBudgetItem> call, @NonNull Response<ServiceBudgetItem> response) {
                if (!response.isSuccessful())
                    errorMessage.postValue("Failed to create service budget item. Code: " + response.code());
                success.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(@NonNull Call<ServiceBudgetItem> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });

        return success;
    }

    public void updateServiceBudgetItem(ServiceBudgetItem serviceBudgetItem) {
        ClientUtils.serviceBudgetItemService.updateServiceBudgetItem(serviceBudgetItem.getId(), serviceBudgetItem.getMaxPrice()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful())
                    errorMessage.postValue("Failed to update service budget item. Code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void deleteServiceBudgetItem(ServiceBudgetItem serviceBudgetItem) {
        ClientUtils.serviceBudgetItemService.deleteServiceBudgetItem(serviceBudgetItem.getEventId(), serviceBudgetItem.getServiceCategoryId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful())
                    errorMessage.postValue("Failed to delete service budget item. Code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }
}
