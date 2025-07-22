package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.products.CatalogueProduct;
import com.wde.eventplanner.models.products.Product;
import com.wde.eventplanner.models.products.EditProductDTO;
import com.wde.eventplanner.models.products.UpdateCatalogueProduct;
import com.wde.eventplanner.utils.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public LiveData<Product> getProduct(String id) {
        MutableLiveData<Product> product = new MutableLiveData<>();
        ClientUtils.productsService.getProductLatestVersion(UUID.fromString(id)).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful()) {
                    product.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch product. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return product;
    }

    public LiveData<EditProductDTO> getEditProduct(String id) {
        MutableLiveData<EditProductDTO> product = new MutableLiveData<>();
        ClientUtils.productsService.getEditProduct(UUID.fromString(id)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EditProductDTO> call, @NonNull Response<EditProductDTO> response) {
                if (response.isSuccessful()) {
                    product.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch product. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EditProductDTO> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return product;
    }

    private static void put(Map<String, RequestBody> map, String key, Object value) {
        if (value != null) map.put(key, toRB(value.toString()));
    }

    private static RequestBody toRB(String value) {
        return RequestBody.create(value, MediaType.parse("text/plain"));
    }

    public LiveData<Product> createProduct(List<File> images, UUID sellerId, String name, Boolean isAvailable, Double price, Double salePercentage, String productCategoryId,
                                           Boolean isPrivate, String description, String suggestedCategory, String suggestedCategoryDescription, List<String> availableEventTypeIds) {
        MutableLiveData<Product> product = new MutableLiveData<>();
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
        put(fields, "productCategoryId", productCategoryId);
        put(fields, "isPrivate", isPrivate);
        put(fields, "description", description);
        put(fields, "suggestedCategory", suggestedCategory);
        put(fields, "suggestedCategoryDescription", suggestedCategoryDescription);

        if (availableEventTypeIds != null)
            for (int i = 0; i < availableEventTypeIds.size(); i++)
                fields.put("availableEventTypeIds[" + i + "]", toRB(availableEventTypeIds.get(i)));

        ClientUtils.productsService.createProduct(fileParts, fields).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful()) {
                    product.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to create product. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });

        return product;
    }

    public LiveData<Product> updateProduct(List<File> images, String staticProductId, String name, Boolean isAvailable, Double price, Double salePercentage,
                                           Boolean isPrivate, String description, List<String> availableEventTypeIds) {
        MutableLiveData<Product> product = new MutableLiveData<>();
        List<MultipartBody.Part> fileParts = new ArrayList<>();
        Map<String, RequestBody> fields = new HashMap<>();

        if (images != null)
            for (File file : images)
                fileParts.add(MultipartBody.Part.createFormData("images", file.getName(), RequestBody.create(file, MediaType.parse("image/*"))));

        put(fields, "staticProductId", staticProductId);
        put(fields, "name", name);
        put(fields, "isAvailable", isAvailable);
        put(fields, "price", price);
        put(fields, "salePercentage", salePercentage);
        put(fields, "isPrivate", isPrivate);
        put(fields, "description", description);

        if (availableEventTypeIds != null)
            for (int i = 0; i < availableEventTypeIds.size(); i++)
                fields.put("availableEventTypeIds[" + i + "]", toRB(availableEventTypeIds.get(i)));

        ClientUtils.productsService.updateProduct(fileParts, fields).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful()) {
                    product.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to update product. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });

        return product;
    }

    public LiveData<Void> deleteProduct(String id) {
        MutableLiveData<Void> done = new MutableLiveData<>();
        ClientUtils.productsService.deleteProduct(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    done.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to delete product. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return done;
    }

    public LiveData<ArrayList<CatalogueProduct>> getCatalogue(String id) {
        MutableLiveData<ArrayList<CatalogueProduct>> catalogue = new MutableLiveData<>();
        ClientUtils.productsService.getCatalogue(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CatalogueProduct>> call, @NonNull Response<ArrayList<CatalogueProduct>> response) {
                if (response.isSuccessful()) {
                    catalogue.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch catalogue. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CatalogueProduct>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return catalogue;
    }

    public LiveData<Void> updateCatalogue(String id, ArrayList<CatalogueProduct> catalogue) {
        MutableLiveData<Void> done = new MutableLiveData<>();
        ClientUtils.productsService.updateCatalogue(id, new UpdateCatalogueProduct(catalogue)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    done.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to update catalogue. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return done;
    }

    public LiveData<File> downloadCatalogue(String id) {
        MutableLiveData<File> file = new MutableLiveData<>();
        ClientUtils.productsService.getPdfCatalogue(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            String fileName = "product_catalogue.pdf";
                            file.postValue(FileManager.saveFileToDownloads(responseBody.byteStream(), fileName));
                        } else
                            errorMessage.postValue("Failed to retrieve PDF. Response body is null.");
                    } catch (IOException e) {
                        errorMessage.postValue("Failed to save pdf catalogue. Error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                errorMessage.postValue("Failed to download pdf catalogue. Code: " + t.getMessage());
            }
        });
        return file;
    }
}
