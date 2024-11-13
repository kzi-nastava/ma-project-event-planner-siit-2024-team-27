package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.titleTextView.setText(event.getTitle());
        holder.timeTextView.setText(event.getTime());
        holder.dateTextView.setText(event.getDate());
        holder.locationTextView.setText(event.getLocation());
        holder.ratingTextView.setText(String.format("%2.1f", event.getRating()));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timeTextView, dateTextView, locationTextView, ratingTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find the views in the layout
            titleTextView = itemView.findViewById(R.id.eventCardTitle);
            timeTextView = itemView.findViewById(R.id.eventCardTime);
            dateTextView = itemView.findViewById(R.id.eventCardDate);
            locationTextView = itemView.findViewById(R.id.eventCardLocation);
            ratingTextView = itemView.findViewById(R.id.eventCardRating);
        }
    }
}