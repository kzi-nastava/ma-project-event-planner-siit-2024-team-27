package com.wde.eventplanner.adapters;

import static java.util.function.UnaryOperator.identity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.wde.eventplanner.databinding.CardEventAgendaItemBinding;
import com.wde.eventplanner.models.event.AgendaItem;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class AgendaItemAdapter extends RecyclerView.Adapter<AgendaItemAdapter.AgendaItemViewHolder> {
    private final FragmentManager fragmentManager;
    private final List<AgendaItem> agendaItems;

    public AgendaItemAdapter(List<AgendaItem> agendaItems, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.agendaItems = agendaItems;
    }

    @NonNull
    @Override
    public AgendaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardEventAgendaItemBinding binding = CardEventAgendaItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AgendaItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaItemViewHolder holder, int position) {
        AgendaItem item = agendaItems.get(position);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        holder.binding.nameInput.setText(item.getName());
        holder.binding.locationInput.setText(item.getLocation());
        holder.binding.descriptionInput.setText(item.getDescription());
        holder.binding.startTimeInput.setText(item.getStartTime());
        holder.binding.endTimeInput.setText(item.getEndTime());

        if (holder.nameWatcher != null)
            holder.binding.nameInput.removeTextChangedListener(holder.nameWatcher);
        if (holder.descriptionWatcher != null)
            holder.binding.locationInput.removeTextChangedListener(holder.descriptionWatcher);
        if (holder.locationWatcher != null)
            holder.binding.descriptionInput.removeTextChangedListener(holder.locationWatcher);

        holder.nameWatcher = new CustomTextWatcher(item::setName);
        holder.descriptionWatcher = new CustomTextWatcher(item::setLocation);
        holder.locationWatcher = new CustomTextWatcher(item::setDescription);

        holder.binding.nameInput.addTextChangedListener(holder.nameWatcher);
        holder.binding.locationInput.addTextChangedListener(holder.descriptionWatcher);
        holder.binding.descriptionInput.addTextChangedListener(holder.locationWatcher);

        MaterialTimePicker startTimePicker = new MaterialTimePicker.Builder()
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select start time")
                .build();

        startTimePicker.addOnPositiveButtonClickListener(v -> {
            item.setStartTime(LocalTime.of(startTimePicker.getHour(), startTimePicker.getMinute()).format(formatter));
            holder.binding.startTimeInput.setText(item.getStartTime());
        });

        MaterialTimePicker endTimePicker = new MaterialTimePicker.Builder()
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select end time")
                .build();

        endTimePicker.addOnPositiveButtonClickListener(v -> {
            item.setEndTime(LocalTime.of(endTimePicker.getHour(), endTimePicker.getMinute()).format(formatter));
            holder.binding.endTimeInput.setText(item.getEndTime());
        });

        holder.binding.startTimeInput.setOnClickListener(v -> startTimePicker.show(fragmentManager, "start_time_picker"));
        holder.binding.endTimeInput.setOnClickListener(v -> endTimePicker.show(fragmentManager, "end_time_picker"));

        holder.binding.deleteButton.setOnClickListener(v -> {
            holder.binding.deleteButton.setOnClickListener(identity()::apply);
            int pos = holder.getAdapterPosition();
            agendaItems.remove(pos);
            notifyItemRemoved(pos);
        });
    }

    @Override
    public int getItemCount() {
        return agendaItems.size();
    }

    public static class AgendaItemViewHolder extends RecyclerView.ViewHolder {
        public CardEventAgendaItemBinding binding;
        public TextWatcher nameWatcher;
        public TextWatcher descriptionWatcher;
        public TextWatcher locationWatcher;

        public AgendaItemViewHolder(CardEventAgendaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class CustomTextWatcher implements TextWatcher {
        private final Consumer<String> consumer;

        private CustomTextWatcher(Consumer<String> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            consumer.accept(charSequence.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
