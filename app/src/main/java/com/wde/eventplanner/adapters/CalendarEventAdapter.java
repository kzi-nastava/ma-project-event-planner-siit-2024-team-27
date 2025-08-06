package com.wde.eventplanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.databinding.CardEventCalendarBinding;
import com.wde.eventplanner.fragments.common.AgendaItemsDialog;
import com.wde.eventplanner.models.event.CalendarEvent;
import com.wde.eventplanner.utils.MenuManager;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.EventViewHolder> {
    private final FragmentManager fragmentManager;
    private final NavController navController;
    public final List<CalendarEvent> events;

    public CalendarEventAdapter(List<CalendarEvent> eventList, NavController navController, FragmentManager fragmentManager) {
        this.events = eventList;
        this.navController = navController;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardEventCalendarBinding binding = CardEventCalendarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        holder.binding.navigateToDetailButton.setOnClickListener(v -> MenuManager.navigateToFragment("EVENT", id, context, navController));

        holder.binding.cardView.setOnClickListener(v -> new AgendaItemsDialog(event.getActivites()).show(fragmentManager, "agendaItemsDialog"));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardEventCalendarBinding binding;

        public EventViewHolder(CardEventCalendarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}