package com.wde.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.EventAdapter;
import com.wde.eventplanner.models.Event;

import java.util.ArrayList;
import java.util.List;

public class AllEventsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_events_screen, container, false);

        Spinner spinner = view.findViewById(R.id.sortSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"Date", "Rating", "Name"});
        spinner.setAdapter(adapter);

        RecyclerView listingsRecyclerView = view.findViewById(R.id.listingsRecyclerView);
        listingsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listingsRecyclerView.setNestedScrollingEnabled(false);

        List<Event> eventList = new ArrayList<>();
        eventList.add(new Event("Best party ever", "10:00", "13.11.2024.", "Novi Sad", 4.5f));
        eventList.add(new Event("Greatest party ever", "12:00", "14.11.2024.", "Novi Sad", 3.0f));
        eventList.add(new Event("Best party ever", "10:00", "13.11.2024.", "Novi Sad", 4.5f));
        eventList.add(new Event("Greatest party ever", "12:00", "14.11.2024.", "Novi Sad", 3.0f));
        eventList.add(new Event("Best party ever", "10:00", "13.11.2024.", "Novi Sad", 4.5f));
        eventList.add(new Event("Best party ever", "10:00", "13.11.2024.", "Novi Sad", 4.5f));
        eventList.add(new Event("Greatest party ever", "12:00", "14.11.2024.", "Novi Sad", 3.0f));
        eventList.add(new Event("Best party ever", "10:00", "13.11.2024.", "Novi Sad", 4.5f));
        eventList.add(new Event("Greatest party ever", "12:00", "14.11.2024.", "Novi Sad", 3.0f));
        eventList.add(new Event("Best party ever", "10:00", "13.11.2024.", "Novi Sad", 4.5f));

        EventAdapter eventAdapter = new EventAdapter(eventList);
        listingsRecyclerView.setAdapter(eventAdapter);

        FilterDialogFragment filterDialog = new FilterDialogFragment();
        Button filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> filterDialog.show(getParentFragmentManager(), "filterDialog"));

        return view;
    }
}
