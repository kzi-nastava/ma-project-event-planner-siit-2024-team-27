package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.ItemGuestListBinding;
import com.wde.eventplanner.models.GuestInfo;

import java.util.ArrayList;
import java.util.List;

public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestInfoViewHolder> {
    public final List<GuestInfo> guests;

    public GuestListAdapter() {
        this.guests = new ArrayList<>();
    }

    public GuestListAdapter(List<GuestInfo> guestList) {
        this.guests = guestList;
    }

    @NonNull
    @Override
    public GuestListAdapter.GuestInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGuestListBinding binding = ItemGuestListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GuestListAdapter.GuestInfoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestListAdapter.GuestInfoViewHolder holder, int position) {
        GuestInfo guest = guests.get(position);
        if (guest.getName() != null && !guest.getName().isBlank() && guest.getSurname() != null && !guest.getSurname().isBlank())
            holder.binding.nameText.setText(String.format("%s %s", guest.getName(), guest.getSurname()));
        else
            holder.binding.nameText.setText(R.string.anonymous);
        holder.binding.emailText.setText(guest.getEmail());
    }

    @Override
    public int getItemCount() {
        return guests.size();
    }

    public static class GuestInfoViewHolder extends RecyclerView.ViewHolder {
        ItemGuestListBinding binding;

        public GuestInfoViewHolder(ItemGuestListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
