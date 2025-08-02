package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.CardNotificationBinding;
import com.wde.eventplanner.models.notification.Notification;
import com.wde.eventplanner.utils.MenuManager;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {
    public final List<Notification> notifications;
    private final NavController navController;

    public NotificationsAdapter(NavController navController) {
        this.notifications = new ArrayList<>();
        this.navController = navController;
    }

    public NotificationsAdapter(List<Notification> notifications, NavController navController) {
        this.navController = navController;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardNotificationBinding binding = CardNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.binding.titleTextView.setText(notification.getTitle());
        holder.binding.messageTextView.setText(notification.getMessage());
        holder.binding.dateTextView.setText(DateTimeFormatter.ofPattern("HH:mm   d.M.yyyy.", Locale.ENGLISH).format(notification.getTime()));
        int color = notification.isSeen() ? R.color.card : R.color.edge;
        holder.binding.notificationCard.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(color, null));
        holder.binding.notificationCard.setOnClickListener(v -> {
            MenuManager.navigateToFragment(notification.getType(), notification.getEntityId().toString(), holder.binding.getRoot().getContext(), navController);
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        CardNotificationBinding binding;

        public NotificationViewHolder(CardNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}