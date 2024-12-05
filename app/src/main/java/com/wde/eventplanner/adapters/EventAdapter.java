package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.R;
import com.wde.eventplanner.models.Event;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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
        holder.titleTextView.setText(event.getName());
        Picasso.get().load(event.getImages().get(0)).into(holder.eventCardPicture);
        holder.titleTextView.setText(event.getName());
        holder.timeTextView.setText(event.getDate().format(DateTimeFormatter.ofPattern("HH:mm")));
        holder.dateTextView.setText(event.getDate().format(DateTimeFormatter.ofPattern("d.M.yyyy.")));
        holder.locationTextView.setText(event.getCity());
        holder.ratingTextView.setText(String.format(Locale.ENGLISH, "%2.1f", event.getRating()));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timeTextView, dateTextView, locationTextView, ratingTextView;
        ImageView eventCardPicture;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventCardPicture = itemView.findViewById(R.id.eventCardPicture);
            titleTextView = itemView.findViewById(R.id.eventCardTitle);
            timeTextView = itemView.findViewById(R.id.eventCardTime);
            dateTextView = itemView.findViewById(R.id.eventCardDate);
            locationTextView = itemView.findViewById(R.id.eventCardLocation);
            ratingTextView = itemView.findViewById(R.id.eventCardRating);
        }
    }
}