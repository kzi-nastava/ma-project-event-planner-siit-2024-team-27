package com.wde.eventplanner.adapters;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardEventTypeBinding;
import com.wde.eventplanner.fragments.admin.event_categories.EditEventTypeDialogFragment;
import com.wde.eventplanner.fragments.admin.event_categories.EventTypesFragment;
import com.wde.eventplanner.models.EventType;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;

import java.util.ArrayList;
import java.util.List;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.EventTypeListAdapterHolder> {
    private final EventTypesFragment parent;
    private EventTypesViewModel viewModel;
    public final List<EventType> types;

    public EventTypeAdapter(EventTypesFragment parent) {
        this.types = new ArrayList<>();
        this.parent = parent;
    }

    public EventTypeAdapter(List<EventType> types, EventTypesFragment parent) {
        this.parent = parent;
        this.types = types;
    }

    @NonNull
    @Override
    public EventTypeListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardEventTypeBinding binding = CardEventTypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        viewModel = new ViewModelProvider(this.parent.requireActivity()).get(EventTypesViewModel.class);
        return new EventTypeListAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeListAdapterHolder holder, int position) {
        EventType type = types.get(position);
        holder.binding.titleView.setText(type.getName());
        holder.binding.descriptionView.setText(type.getDescription());
        holder.binding.statusSwitch.setChecked(type.getIsActive());
        holder.binding.titleView.setTextColor(type.getIsActive() ? Color.WHITE : Color.GRAY);
        holder.binding.descriptionView.setTextColor(type.getIsActive() ? Color.WHITE : Color.GRAY);
        holder.binding.editButton.setColorFilter(type.getIsActive() ? null : new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN));

        holder.binding.statusSwitch.setOnClickListener((v) -> {
            type.setIsActive(!type.getIsActive());
            holder.binding.titleView.setTextColor(!type.getIsActive() ? Color.WHITE : Color.GRAY);
            holder.binding.descriptionView.setTextColor(!type.getIsActive() ? Color.WHITE : Color.GRAY);
            holder.binding.editButton.setColorFilter(!type.getIsActive() ? null : new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN));
            notifyItemChanged(position);
            holder.binding.getRoot().postDelayed(() -> viewModel.editEventType(type.getId(), type), 250);
        });

        holder.binding.editButton.setOnClickListener(v -> new AlertDialog.Builder(holder.itemView.getContext()).create().show());
        EditEventTypeDialogFragment editDialog = new EditEventTypeDialogFragment(type);
        holder.binding.editButton.setOnClickListener(v -> editDialog.show(parent.getParentFragmentManager(), "editDialog"));
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public static class EventTypeListAdapterHolder extends RecyclerView.ViewHolder {
        CardEventTypeBinding binding;

        public EventTypeListAdapterHolder(CardEventTypeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
