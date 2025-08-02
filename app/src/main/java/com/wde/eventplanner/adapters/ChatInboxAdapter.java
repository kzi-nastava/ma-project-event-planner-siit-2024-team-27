package com.wde.eventplanner.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.CardInboxItemBinding;
import com.wde.eventplanner.models.chat.Chat;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ChatInboxAdapter extends RecyclerView.Adapter<ChatInboxAdapter.ChatInboxViewHolder> {
    public final List<Chat> chats;

    public ChatInboxAdapter(List<Chat> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatInboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardInboxItemBinding binding = CardInboxItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChatInboxViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatInboxViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.binding.title.setText(chat.getListingName());
        holder.binding.message.setText(chat.getLastMessage());
        holder.binding.user.setText(chat.getChatPartnerNameAndSurname());
        if (chat.getLastMessageDate() != null)
            holder.binding.dateTextView.setText(DateTimeFormatter.ofPattern("d.M.yyyy.", Locale.ENGLISH).format(chat.getLastMessageDate()));
        else
            holder.binding.dateTextView.setText("");
        holder.binding.chatCard.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("chatId", chat.getChatId().toString());
            Navigation.findNavController(v).navigate(R.id.action_inbox_to_chat, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ChatInboxViewHolder extends RecyclerView.ViewHolder {
        CardInboxItemBinding binding;

        public ChatInboxViewHolder(CardInboxItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}