package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.CardNotificationBinding;
import com.wde.eventplanner.databinding.ItemCarouselImageBinding;
import com.wde.eventplanner.models.Notification;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {
    private List<Notification> notifications;

    public NotificationsAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardNotificationBinding binding = CardNotificationBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.binding.titleTextView.setText(notification.getTitle());
        holder.binding.messageTextView.setText(notification.getMessage());
        holder.binding.dateTextView.setText(new SimpleDateFormat("d.MM.yyyy.").format(notification.getDate()));
        if (!notification.isSeen())
            holder.binding.notificationCard.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.edge, null));
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