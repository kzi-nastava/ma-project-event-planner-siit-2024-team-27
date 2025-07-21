package com.wde.eventplanner.fragments.seller.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.CalendarEventDialogAdapter;
import com.wde.eventplanner.databinding.DialogCalendarItemsBinding;
import com.wde.eventplanner.models.event.CalendarEvent;

import java.util.ArrayList;

public class CalendarEventsDialog extends DialogFragment {
    private final ArrayList<CalendarEvent> events;
    private final NavController navController;

    public CalendarEventsDialog(ArrayList<CalendarEvent> events, NavController navController) {
        this.navController = navController;
        this.events = events;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogCalendarItemsBinding binding = DialogCalendarItemsBinding.inflate(inflater, container, false);

        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        binding.title.setText(R.string.events);
        binding.items.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.items.setAdapter(new CalendarEventDialogAdapter(events, navController, this::dismiss));
        binding.closeButton.setOnClickListener(v -> dismiss());

        return binding.getRoot();
    }
}
