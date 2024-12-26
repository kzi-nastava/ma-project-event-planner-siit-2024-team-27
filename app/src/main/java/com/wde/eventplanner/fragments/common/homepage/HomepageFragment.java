package com.wde.eventplanner.fragments.common.homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.EventAdapter;
import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.databinding.FragmentHomepageBinding;
import com.wde.eventplanner.models.event.Event;
import com.wde.eventplanner.models.listing.Listing;
import com.wde.eventplanner.viewmodels.EventsViewModel;
import com.wde.eventplanner.viewmodels.ListingsViewModel;

import java.util.ArrayList;

public class HomepageFragment extends Fragment {
    private FragmentHomepageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomepageBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        binding.listingsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.listingsRecyclerView.setNestedScrollingEnabled(false);
        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.eventsRecyclerView.setNestedScrollingEnabled(false);

        ListingsViewModel listingsViewModel = viewModelProvider.get(ListingsViewModel.class);
        listingsViewModel.getTopListings().observe(getViewLifecycleOwner(), this::listingsChanged);
        listingsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                listingsViewModel.clearErrorMessage();
            }
        });

        EventsViewModel eventsViewModel = viewModelProvider.get(EventsViewModel.class);
        eventsViewModel.getTopEvents().observe(getViewLifecycleOwner(), this::eventsChanged);
        eventsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                eventsViewModel.clearErrorMessage();
            }
        });

        binding.listingsRecyclerView.setAdapter(listingsViewModel.getTopListings().isInitialized() ?
                new ListingAdapter(listingsViewModel.getTopListings().getValue()) : new ListingAdapter());

        binding.eventsRecyclerView.setAdapter(eventsViewModel.getTopEvents().isInitialized() ?
                new EventAdapter(eventsViewModel.getTopEvents().getValue()) : new EventAdapter());

        listingsViewModel.fetchTopListings();
        listingsViewModel.fetchListings();
        eventsViewModel.fetchTopEvents();
        eventsViewModel.fetchEvents();

        return binding.getRoot();
    }

    private void listingsChanged(ArrayList<Listing> listings) {
        if (binding.listingsRecyclerView.getAdapter() != null) {
            ListingAdapter adapter = (ListingAdapter) binding.listingsRecyclerView.getAdapter();
            ArrayList<Listing> listingsTmp = new ArrayList<>(listings);
            adapter.listings.clear();
            adapter.listings.addAll(listingsTmp);
            adapter.notifyItemRangeChanged(0, 5);
        }
    }

    private void eventsChanged(ArrayList<Event> events) {
        if (binding.eventsRecyclerView.getAdapter() != null) {
            EventAdapter adapter = (EventAdapter) binding.eventsRecyclerView.getAdapter();
            ArrayList<Event> eventsTmp = new ArrayList<>(events);
            adapter.events.clear();
            adapter.events.addAll(eventsTmp);
            adapter.notifyItemRangeChanged(0, 5);
        }
    }
}
