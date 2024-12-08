package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingCategoriesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ListingCategory>> pendingListingCategories = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ListingCategory>> activeListingCategories = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<ListingCategory>> getPendingListingCategories() {
        return pendingListingCategories;
    }

    public LiveData<ArrayList<ListingCategory>> getActiveListingCategories() {
        return activeListingCategories;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchActiveListingCategories() {
        ClientUtils.listingCategoriesService.getCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ListingCategory>> call, @NonNull Response<ArrayList<ListingCategory>> response) {
                if (response.isSuccessful()) {
                    activeListingCategories.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch active listing categories. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ListingCategory>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void deleteActiveListingCategory(ListingCategory listingCategory) {
        ClientUtils.listingCategoriesService.deleteListingCategory(listingCategory.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategory> activeListWithDTODeleted = activeListingCategories.getValue();
                    if (activeListWithDTODeleted != null) {
                        activeListWithDTODeleted.removeIf(c -> Objects.equals(c.getId(), listingCategory.getId()));
                        listingCategory.setIsDeleted(true);
                        activeListWithDTODeleted.add(listingCategory);
                        activeListingCategories.setValue(new ArrayList<>(activeListWithDTODeleted));
                    }
                } else {
                    try {
                        errorMessage.postValue("Failed to delete active listing category: " + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void editActiveListingCategory(String id, ListingCategory listingCategory) {
        ClientUtils.listingCategoriesService.updateListingCategory(id, listingCategory).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListingCategory> call, @NonNull Response<ListingCategory> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategory> activeListWithDTOEdited = activeListingCategories.getValue();
                    if (activeListWithDTOEdited != null) {
                        activeListWithDTOEdited.removeIf(c -> Objects.equals(c.getId(), id));
                        activeListWithDTOEdited.add(response.body());
                        activeListingCategories.setValue(new ArrayList<>(activeListWithDTOEdited));
                    }
                } else {
                    errorMessage.postValue("Failed to edit listing category. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListingCategory> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void createActiveListingCategory(ListingCategory listingCategory) {
        ClientUtils.listingCategoriesService.createActiveListingCategory(listingCategory).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListingCategory> call, @NonNull Response<ListingCategory> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategory> activeListWithDTOAdded = activeListingCategories.getValue();
                    if (activeListWithDTOAdded != null) {
                        activeListWithDTOAdded.add(response.body());
                        activeListingCategories.setValue(new ArrayList<>(activeListWithDTOAdded));
                    }
                } else {
                    errorMessage.postValue("Failed to create active listing category. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListingCategory> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchPendingListingCategories() {
        ClientUtils.listingCategoriesService.getPendingCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ListingCategory>> call, @NonNull Response<ArrayList<ListingCategory>> response) {
                if (response.isSuccessful()) {
                    pendingListingCategories.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch pending listing categories. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ListingCategory>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void editPendingListingCategory(String id, ListingCategory listingCategory) {
        ClientUtils.listingCategoriesService.updateListingCategory(id, listingCategory).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListingCategory> call, @NonNull Response<ListingCategory> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategory> pendingListWithDTOEdited = pendingListingCategories.getValue();
                    if (pendingListWithDTOEdited != null) {
                        pendingListWithDTOEdited.removeIf(c -> Objects.equals(c.getId(), id));
                        pendingListWithDTOEdited.add(response.body());
                        pendingListingCategories.setValue(new ArrayList<>(pendingListWithDTOEdited));
                    }
                } else {
                    errorMessage.postValue("Failed to edit listing category. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListingCategory> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void approvePendingListingCategory(ListingCategory listingCategory) {
        ClientUtils.listingCategoriesService.approvePendingListingCategory(listingCategory.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListingCategory> call, @NonNull Response<ListingCategory> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategory> pendingListWithDTORemoved = pendingListingCategories.getValue();
                    if (pendingListWithDTORemoved != null) {
                        pendingListWithDTORemoved.removeIf(c -> Objects.equals(c.getId(), listingCategory.getId()));
                        pendingListingCategories.setValue(new ArrayList<>(pendingListWithDTORemoved));
                    }

                    ArrayList<ListingCategory> activeListWithDTOAdded = activeListingCategories.getValue();
                    if (activeListWithDTOAdded != null) {
                        activeListWithDTOAdded.add(response.body());
                        activeListingCategories.setValue(new ArrayList<>(activeListWithDTOAdded));
                    }
                } else {
                    errorMessage.postValue("Failed to fetch approve pending listing category. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListingCategory> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void replacePendingListingCategory(ReplacingListingCategory replacingListingCategory) {
        ClientUtils.listingCategoriesService.replacePendingListingCategory(replacingListingCategory).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategory> pendingListWithDTODeleted = pendingListingCategories.getValue();
                    if (pendingListWithDTODeleted != null) {
                        pendingListWithDTODeleted.removeIf(c -> Objects.equals(c.getId(), replacingListingCategory.getToBeReplacedId()));
                        pendingListingCategories.setValue(new ArrayList<>(pendingListWithDTODeleted));
                    }
                } else {
                    errorMessage.postValue("Failed to replace pending listing category. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
