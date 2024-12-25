package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.user.Token;
import com.wde.eventplanner.models.user.User;

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
}
