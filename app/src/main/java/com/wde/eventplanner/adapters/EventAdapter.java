package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.databinding.CardEventBinding;
import com.wde.eventplanner.models.event.Event;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    public final List<Event> events;

    public EventAdapter() {
        this.events = new ArrayList<>();
    }

    public EventAdapter(List<Event> eventList) {
        this.events = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardEventBinding binding = CardEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        Picasso.get().load(event.getImages().get(0)).into(holder.binding.eventCardPicture);
        holder.binding.eventCardTitle.setText(event.getName());
        holder.binding.eventCardTime.setText(event.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        holder.binding.eventCardDate.setText(event.getDate().format(DateTimeFormatter.ofPattern("d.M.yyyy.")));
        holder.binding.eventCardLocation.setText(event.getCity());
        holder.binding.eventCardRating.setText(String.format(Locale.ENGLISH, "%2.1f", event.getRating()));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardEventBinding binding;

        public EventViewHolder(CardEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}