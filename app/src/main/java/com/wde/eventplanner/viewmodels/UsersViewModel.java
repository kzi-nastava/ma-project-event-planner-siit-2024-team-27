package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.user.RegistrationRequest;
import com.wde.eventplanner.models.user.Token;
import com.wde.eventplanner.models.user.User;

import java.io.File;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersViewModel extends ViewModel {
    public LiveData<Token> login(User user) {
        MutableLiveData<Token> token = new MutableLiveData<>();

        ClientUtils.usersService.login(user).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                token.postValue(response.isSuccessful() ? response.body() : new Token(null));
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                token.postValue(new Token(null));
            }
        });

        return token;
    }

    public LiveData<String> register(RegistrationRequest registrationRequest) {
        MutableLiveData<String> responseMessage = new MutableLiveData<>();

        ClientUtils.usersService.register(registrationRequest).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                responseMessage.postValue(response.isSuccessful() ? response.body() : "");
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                responseMessage.postValue("");
            }
        });

        return responseMessage;
    }

    public LiveData<Response<String>> putImage(File imageFile, UUID profileId) {
        MutableLiveData<Response<String>> responseMessage = new MutableLiveData<>();
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("imageFile", imageFile.getName(), fileRequestBody);
        RequestBody profileIdRequestBody = RequestBody.create(MediaType.parse("text/plain"), profileId.toString());

        ClientUtils.usersService.putImage(filePart, profileIdRequestBody).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                responseMessage.postValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Response<String> errorResponse = Response.error(500,
                        ResponseBody.create(MediaType.parse("text/plain"), "Failed to add the image"));
                responseMessage.postValue(errorResponse);
            }
        });

        return responseMessage;
    }
}
