package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.chat.Chat;
import com.wde.eventplanner.models.chat.ChatMessage;
import com.wde.eventplanner.models.chat.CreateChat;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatsService {
    @GET("chats/{profileId}")
    Call<ArrayList<Chat>> getChats(@Path("profileId") UUID profileId);

    @GET("chats/{profileId}/{chatId}")
    Call<Chat> getChat(@Path("profileId") UUID profileId, @Path("chatId") UUID chatId);

    @GET("chats/messages/{chatId}")
    Call<ArrayList<ChatMessage>> getMessages(@Path("chatId") UUID chatId);

    @POST("chats")
    Call<Chat> createChat(@Body CreateChat chat);
}
