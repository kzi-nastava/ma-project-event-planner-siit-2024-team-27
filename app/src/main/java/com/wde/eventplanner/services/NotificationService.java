package com.wde.eventplanner.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wde.eventplanner.R;
import com.wde.eventplanner.activities.MainActivity;

import java.util.Map;
import java.util.UUID;

public class NotificationService extends FirebaseMessagingService {
    public static Runnable onMessageReceived;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            if (onMessageReceived != null) onMessageReceived.run();
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            PendingIntent pendingIntent = null;

            if (!remoteMessage.getData().isEmpty()) {
                Map<String, String> data = remoteMessage.getData();
                String type = data.get("type");
                String entityId = data.get("entityId");

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("entityId", entityId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            }

            showNotification(title, body, pendingIntent);
        }
    }

    private void showNotification(String title, String message, PendingIntent intent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "user_notifications")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        if (intent != null) builder = builder.setContentIntent(intent);
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
    }

    public static void subscribe(UUID profileId) {
        FirebaseMessaging.getInstance().deleteToken();
        FirebaseMessaging.getInstance().subscribeToTopic("user_" + profileId);
    }

    public static void unsubscribe() {
        FirebaseMessaging.getInstance().deleteToken();
    }
}