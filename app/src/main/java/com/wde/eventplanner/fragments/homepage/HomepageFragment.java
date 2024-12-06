package com.wde.eventplanner.fragments.homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.EventAdapter;
import com.wde.eventplanner.adapters.ListingAdapter;

public class HomepageFragment extends Fragment {
    private ListingsViewModel listingsViewModel;
    private EventsViewModel eventsViewModel;
    private RecyclerView listingsRecyclerView;
    private RecyclerView eventsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage_screen, container, false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        ViewModelProvider viewModelProvider = new ViewModelProvider(navController.getBackStackEntry(R.id.HomepageFragment));

        listingsViewModel = viewModelProvider.get(ListingsViewModel.class);
        eventsViewModel = viewModelProvider.get(EventsViewModel.class);

        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        eventsRecyclerView.setNestedScrollingEnabled(false);

        listingsRecyclerView = view.findViewById(R.id.listingsRecyclerView);
        listingsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listingsRecyclerView.setNestedScrollingEnabled(false);

        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventsViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            eventsRecyclerView.setAdapter(new EventAdapter(events));
        });

        listingsViewModel.getListings().observe(getViewLifecycleOwner(), listings -> {
            listingsRecyclerView.setAdapter(new ListingAdapter(listings));
        });

        eventsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        listingsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        eventsViewModel.fetchTopEvents();
        listingsViewModel.fetchTopListings();
    }
}
