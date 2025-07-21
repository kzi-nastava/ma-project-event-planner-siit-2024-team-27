package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardAgendaItemBinding;
import com.wde.eventplanner.models.event.AgendaItem;

import java.util.List;

public class CalendarAgendaAdapter extends RecyclerView.Adapter<CalendarAgendaAdapter.EventViewHolder> {
    public final List<AgendaItem> agendaItems;

    public CalendarAgendaAdapter(List<AgendaItem> agenda) {
        this.agendaItems = agenda;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardAgendaItemBinding binding = CardAgendaItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        AgendaItem agendaItem = agendaItems.get(position);
        holder.binding.title.setText(agendaItem.getName());
        holder.binding.description.setText(agendaItem.getDescription());
        holder.binding.time.setText(agendaItem.getStartTime());
        holder.binding.location.setText(agendaItem.getLocation());
    }

    @Override
    public int getItemCount() {
        return agendaItems.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardAgendaItemBinding binding;

        public EventViewHolder(CardAgendaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}