package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.productBudgetItem.BuyProductDTO;
import com.wde.eventplanner.models.productBudgetItem.ProductBudgetItemDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductBudgetItemViewModel extends ViewModel {
    private final MutableLiveData<ProductBudgetItemDTO> productBudgetItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ProductBudgetItemDTO> getProductBudgetItem() {
        return productBudgetItemLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void buyProduct(BuyProductDTO buyProductDTO) {
        ClientUtils.productBudgetItemService.buyProduct(buyProductDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ProductBudgetItemDTO> call, @NonNull Response<ProductBudgetItemDTO> response) {
                if (response.isSuccessful()) {
                    productBudgetItemLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to buy product. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductBudgetItemDTO> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void createProductBudgetItem(ProductBudgetItemDTO createProductBudgetItem) {
        ClientUtils.productBudgetItemService.createProductBudgetItem(createProductBudgetItem).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ProductBudgetItemDTO> call, @NonNull Response<ProductBudgetItemDTO> response) {
                if (response.isSuccessful()) {
                    productBudgetItemLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to create product budget item. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductBudgetItemDTO> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void updateProductBudgetItem(ProductBudgetItemDTO productBudgetItem) {
        ClientUtils.productBudgetItemService.updateProductBudgetItem(productBudgetItem.getId(), productBudgetItem.getMaxPrice()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful())
                    errorMessage.postValue("Failed to update product budget item. Code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void deleteProductBudgetItem(ProductBudgetItemDTO productBudgetItem) {
        ClientUtils.productBudgetItemService.deleteProductBudgetItem(productBudgetItem.getEventId(), productBudgetItem.getProductCategoryId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful())
                    errorMessage.postValue("Failed to delete product budget item. Code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }
}
