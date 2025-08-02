package com.wde.eventplanner.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.CardMessageBinding;
import com.wde.eventplanner.models.chat.ChatMessage;
import com.wde.eventplanner.utils.TokenManager;

import java.util.List;
import java.util.UUID;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatViewHolder> {
    public final List<ChatMessage> messages;
    private UUID profileId;

    public ChatMessageAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardMessageBinding binding = CardMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        profileId = TokenManager.getProfileId(binding.getRoot().getContext());
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.binding.message.setText(message.getMessage());
        Context context = holder.binding.getRoot().getContext();

        if (profileId.equals(message.getToProfileId())) {
            holder.binding.messageCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.edge));
            holder.binding.messageDirection.setGravity(Gravity.START);
            holder.binding.spaceRight.setVisibility(VISIBLE);
            holder.binding.spaceLeft.setVisibility(GONE);
        } else {
            holder.binding.messageCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accent2));
            holder.binding.messageDirection.setGravity(Gravity.END);
            holder.binding.spaceLeft.setVisibility(VISIBLE);
            holder.binding.spaceRight.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        CardMessageBinding binding;

        public ChatViewHolder(CardMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}