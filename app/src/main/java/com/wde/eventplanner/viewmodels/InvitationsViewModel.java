package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.event.GuestInfo;
import com.wde.eventplanner.models.event.GuestList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationsViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public LiveData<GuestInfo> fetchGuestInfo(String email) {
        MutableLiveData<GuestInfo> guestInfo = new MutableLiveData<>();
        ClientUtils.invitationService.getGuestInfo(email).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GuestInfo> call, @NonNull Response<GuestInfo> response) {
                if (response.isSuccessful()) {
                    guestInfo.postValue(response.body());
                } else {
                    guestInfo.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GuestInfo> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return guestInfo;
    }

    public LiveData<String> sendInvitations(GuestList guestList) {
        errorMessage.setValue(null);
        ClientUtils.invitationService.sendInvitations(guestList).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful())
                    errorMessage.postValue("Failed to send invitations. Code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
        return errorMessage;
    }
}
