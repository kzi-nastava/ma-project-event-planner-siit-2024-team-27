package com.wde.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.EventAdapter;
import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.models.Event;
import com.wde.eventplanner.models.Listing;

import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage_screen, container, false);

        RecyclerView eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        eventsRecyclerView.setNestedScrollingEnabled(false);

        List<Event> eventList = new ArrayList<>();
        eventList.add(new Event("Best party ever", "10:00", "13.11.2024.", "Novi Sad", 4.5f));
        eventList.add(new Event("Greatest party ever", "12:00", "14.11.2024.", "Novi Sad", 3.0f));
        eventList.add(new Event("Best party ever", "10:00", "13.11.2024.", "Novi Sad", 4.5f));
        eventList.add(new Event("Greatest party ever", "12:00", "14.11.2024.", "Novi Sad", 3.0f));
        eventList.add(new Event("Best party ever", "10:00", "13.11.2024.", "Novi Sad", 4.5f));

        EventAdapter eventAdapter = new EventAdapter(eventList);
        eventsRecyclerView.setAdapter(eventAdapter);


        RecyclerView listingsRecyclerView = view.findViewById(R.id.listingsRecyclerView);
        listingsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listingsRecyclerView.setNestedScrollingEnabled(false);

        List<Listing> listingsList = new ArrayList<>();
        listingsList.add(new Listing("Best product ever", "200€", "199.99€", 4.5f));
        listingsList.add(new Listing("Best service ever", "60€/hr", "59.99€/hr", 4.5f));
        listingsList.add(new Listing("Best product ever", "200€", "199.99€", 4.5f));
        listingsList.add(new Listing("Best service ever", "60€/hr", "59.99€/hr", 4.5f));
        listingsList.add(new Listing("Best product ever", "200€", "199.99€", 4.5f));

        ListingAdapter listingAdapter = new ListingAdapter(listingsList);
        listingsRecyclerView.setAdapter(listingAdapter);

        return view;
    }
}
