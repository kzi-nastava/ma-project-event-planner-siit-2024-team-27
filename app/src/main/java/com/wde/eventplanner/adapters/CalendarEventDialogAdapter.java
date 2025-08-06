package com.wde.eventplanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.databinding.CardEventCalendarDialogBinding;
import com.wde.eventplanner.models.event.CalendarEvent;
import com.wde.eventplanner.utils.MenuManager;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarEventDialogAdapter extends RecyclerView.Adapter<CalendarEventDialogAdapter.EventViewHolder> {
    private final NavController navController;
    public final List<CalendarEvent> events;
    public final Runnable dismiss;

    public CalendarEventDialogAdapter(List<CalendarEvent> eventList, NavController navController, Runnable dismiss) {
        this.navController = navController;
        this.events = eventList;
        this.dismiss = dismiss;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardEventCalendarDialogBinding binding = CardEventCalendarDialogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        CalendarEvent event = events.get(position);
        if (!event.getImages().isEmpty())
            Picasso.get().load(event.getImages().get(0)).into(holder.binding.eventCardPicture);
        else
            holder.binding.eventCardPicture.setImageDrawable(new ColorDrawable(Color.parseColor("#303030")));
        holder.binding.eventCardTitle.setText(event.getName());
        holder.binding.eventCardTime.setText(event.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        String id = event.getId();
        Context context = holder.binding.getRoot().getContext();
        holder.binding.navigateToDetailButton.setOnClickListener(v -> {
            MenuManager.navigateToFragment("EVENT", id, context, navController);
            dismiss.run();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardEventCalendarDialogBinding binding;

        public EventViewHolder(CardEventCalendarDialogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}