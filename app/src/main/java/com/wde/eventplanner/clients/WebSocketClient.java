package com.wde.eventplanner.clients;

import android.content.Context;

import com.wde.eventplanner.BuildConfig;
import com.wde.eventplanner.models.chat.ChatMessage;
import com.wde.eventplanner.utils.SingleToast;

import java.util.function.Consumer;
import java.util.function.Function;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketClient {
    private static final String SOCKET_URL = "ws://" + BuildConfig.IP_ADDR + ":8080/chat-socket-mobile";
    private static StompClient stompClient;

    private static void connect() {
        if (stompClient == null) {
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL);
            stompClient.connect();
        }
    }

    public static void setListener(Context context, String chatId, Consumer<ChatMessage> consumer) {
        connect();
        Disposable d = stompClient.topic("/chat-socket-publisher/" + chatId).subscribe(
                topicMessage -> consumer.accept(ClientUtils.gson.fromJson(topicMessage.getPayload(), ChatMessage.class)),
                throwable -> SingleToast.show(context, "Unable to connect to server!"));
    }

    public static void sendMessage(Context context, ChatMessage message) {
        connect();
        String json = ClientUtils.gson.toJson(message);
        Disposable d = stompClient.send("/chat-socket-subscriber/send-message", json).subscribe(Function::identity,
                throwable -> SingleToast.show(context, "Unable to send the message!"));
    }
}
