package com.wde.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardServiceCalendarBinding;
import com.wde.eventplanner.fragments.seller.calendar.CalendarEventsDialog;
import com.wde.eventplanner.models.event.CalendarService;
import com.wde.eventplanner.utils.MenuManager;

import java.util.List;

public class CalendarServiceAdapter extends RecyclerView.Adapter<CalendarServiceAdapter.EventViewHolder> {
    private final FragmentManager fragmentManager;
    private final NavController navController;
    public final List<CalendarService> services;

    public CalendarServiceAdapter(List<CalendarService> services, NavController navController, FragmentManager fragmentManager) {
        this.services = services;
        this.navController = navController;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardServiceCalendarBinding binding = CardServiceCalendarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        CalendarService service = services.get(position);
        holder.binding.title.setText(service.getName());

        if (navController != null) {
            String id = service.getId();
            Context context = holder.binding.getRoot().getContext();
            holder.binding.navigateToDetailButton.setOnClickListener(v -> MenuManager.navigateToFragment("SERVICE", id, context, navController));
        }

        holder.binding.cardView.setOnClickListener(v -> new CalendarEventsDialog(service.getEvents(), navController).show(fragmentManager, "eventsItemsDialog"));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardServiceCalendarBinding binding;

        public EventViewHolder(CardServiceCalendarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}