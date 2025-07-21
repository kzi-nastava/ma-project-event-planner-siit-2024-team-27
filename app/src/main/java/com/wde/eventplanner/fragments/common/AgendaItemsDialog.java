package com.wde.eventplanner.fragments.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.CalendarAgendaAdapter;
import com.wde.eventplanner.databinding.DialogCalendarItemsBinding;
import com.wde.eventplanner.models.event.AgendaItem;

import java.util.ArrayList;

public class AgendaItemsDialog extends DialogFragment {
    private final ArrayList<AgendaItem> agenda;

    public AgendaItemsDialog(ArrayList<AgendaItem> agenda) {
        this.agenda = agenda;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogCalendarItemsBinding binding = DialogCalendarItemsBinding.inflate(inflater, container, false);

        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        binding.items.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.items.setAdapter(new CalendarAgendaAdapter(agenda));
        binding.closeButton.setOnClickListener(v -> dismiss());

        return binding.getRoot();
    }
}
