package com.wde.eventplanner.fragments.AdminListingCategories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategoryDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminListingCategoriesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ListingCategoryDTO>> pendingListingCategories = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ListingCategoryDTO>> activeListingCategories = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<ListingCategoryDTO>> getPendingListingCategories() {
        return pendingListingCategories;
    }

    public LiveData<ArrayList<ListingCategoryDTO>> getActiveListingCategories() {
        return activeListingCategories;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchActiveListingCategories() {
        ClientUtils.listingCategoryService.getCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ListingCategoryDTO>> call, @NonNull Response<ArrayList<ListingCategoryDTO>> response) {
                if (response.isSuccessful()) {
                    activeListingCategories.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch active listing categories. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ListingCategoryDTO>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void deleteActiveListingCategory(ListingCategoryDTO listingCategoryDTO) {
        ClientUtils.listingCategoryService.deleteListingCategory(listingCategoryDTO.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategoryDTO> activeListWithDTODeleted = activeListingCategories.getValue();
                    if (activeListWithDTODeleted != null) {
                        activeListWithDTODeleted.removeIf(c -> Objects.equals(c.getId(), listingCategoryDTO.getId()));
                        listingCategoryDTO.setIsDeleted(true);
                        activeListWithDTODeleted.add(listingCategoryDTO);
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

    public void editActiveListingCategory(String id, ListingCategoryDTO listingCategoryDTO) {
        ClientUtils.listingCategoryService.updateListingCategory(id, listingCategoryDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListingCategoryDTO> call, @NonNull Response<ListingCategoryDTO> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategoryDTO> activeListWithDTOEdited = activeListingCategories.getValue();
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
            public void onFailure(@NonNull Call<ListingCategoryDTO> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void createActiveListingCategory(ListingCategoryDTO listingCategoryDTO) {
        ClientUtils.listingCategoryService.createActiveListingCategory(listingCategoryDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListingCategoryDTO> call, @NonNull Response<ListingCategoryDTO> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategoryDTO> activeListWithDTOAdded = activeListingCategories.getValue();
                    if (activeListWithDTOAdded != null) {
                        activeListWithDTOAdded.add(response.body());
                        activeListingCategories.setValue(new ArrayList<>(activeListWithDTOAdded));
                    }
                } else {
                    errorMessage.postValue("Failed to create active listing category. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListingCategoryDTO> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchPendingListingCategories() {
        ClientUtils.listingCategoryService.getPendingCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ListingCategoryDTO>> call, @NonNull Response<ArrayList<ListingCategoryDTO>> response) {
                if (response.isSuccessful()) {
                    pendingListingCategories.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch pending listing categories. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ListingCategoryDTO>> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void editPendingListingCategory(String id, ListingCategoryDTO listingCategoryDTO) {
        ClientUtils.listingCategoryService.updateListingCategory(id, listingCategoryDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListingCategoryDTO> call, @NonNull Response<ListingCategoryDTO> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategoryDTO> pendingListWithDTOEdited = pendingListingCategories.getValue();
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
            public void onFailure(@NonNull Call<ListingCategoryDTO> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void approvePendingListingCategory(ListingCategoryDTO listingCategoryDTO) {
        ClientUtils.listingCategoryService.approvePendingListingCategory(listingCategoryDTO.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListingCategoryDTO> call, @NonNull Response<ListingCategoryDTO> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategoryDTO> pendingListWithDTORemoved = pendingListingCategories.getValue();
                    if (pendingListWithDTORemoved != null) {
                        pendingListWithDTORemoved.removeIf(c -> Objects.equals(c.getId(), listingCategoryDTO.getId()));
                        pendingListingCategories.setValue(new ArrayList<>(pendingListWithDTORemoved));
                    }

                    ArrayList<ListingCategoryDTO> activeListWithDTOAdded = activeListingCategories.getValue();
                    if (activeListWithDTOAdded != null) {
                        activeListWithDTOAdded.add(response.body());
                        activeListingCategories.setValue(new ArrayList<>(activeListWithDTOAdded));
                    }
                } else {
                    errorMessage.postValue("Failed to fetch approve pending listing category. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListingCategoryDTO> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void replacePendingListingCategory(ReplacingListingCategoryDTO replacingListingCategoryDTO) {
        ClientUtils.listingCategoryService.replacePendingListingCategory(replacingListingCategoryDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    ArrayList<ListingCategoryDTO> pendingListWithDTODeleted = pendingListingCategories.getValue();
                    if (pendingListWithDTODeleted != null) {
                        pendingListWithDTODeleted.removeIf(c -> Objects.equals(c.getId(), replacingListingCategoryDTO.getToBeReplacedId()));
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
