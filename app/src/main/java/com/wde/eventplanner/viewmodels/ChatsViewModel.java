package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.chat.Chat;
import com.wde.eventplanner.models.chat.ChatMessage;
import com.wde.eventplanner.models.chat.CreateChat;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public LiveData<ArrayList<Chat>> getChats(UUID profileId) {
        MutableLiveData<ArrayList<Chat>> chats = new MutableLiveData<>();
        ClientUtils.chatsService.getChats(profileId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Chat>> call, @NonNull Response<ArrayList<Chat>> response) {
                if (response.isSuccessful()) {
                    chats.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch chats. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Chat>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return chats;
    }

    public LiveData<Chat> getChat(UUID profileId, UUID chatId) {
        MutableLiveData<Chat> chat = new MutableLiveData<>();
        ClientUtils.chatsService.getChat(profileId, chatId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Chat> call, @NonNull Response<Chat> response) {
                if (response.isSuccessful()) {
                    chat.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch chat. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Chat> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return chat;
    }

    public LiveData<ArrayList<ChatMessage>> getMessages(UUID chatId) {
        MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<>();
        ClientUtils.chatsService.getMessages(chatId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ChatMessage>> call, @NonNull Response<ArrayList<ChatMessage>> response) {
                if (response.isSuccessful()) {
                    messages.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch messages. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ChatMessage>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return messages;
    }

    public LiveData<Chat> createChat(CreateChat createChat) {
        MutableLiveData<Chat> chat = new MutableLiveData<>();
        ClientUtils.chatsService.createChat(createChat).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Chat> call, @NonNull Response<Chat> response) {
                if (response.isSuccessful()) {
                    chat.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to create chat. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Chat> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return chat;
    }
}
