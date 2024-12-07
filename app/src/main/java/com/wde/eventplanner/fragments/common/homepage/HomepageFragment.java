package com.wde.eventplanner.fragments.common.homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.EventAdapter;
import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.databinding.FragmentHomepageBinding;
import com.wde.eventplanner.viewmodels.EventsViewModel;
import com.wde.eventplanner.viewmodels.ListingsViewModel;

public class HomepageFragment extends Fragment {
    private ListingsViewModel listingsViewModel;
    private EventsViewModel eventsViewModel;
    private FragmentHomepageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomepageBinding.inflate(inflater, container, false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        ViewModelProvider viewModelProvider = new ViewModelProvider(navController.getBackStackEntry(R.id.HomepageFragment));

        listingsViewModel = viewModelProvider.get(ListingsViewModel.class);
        eventsViewModel = viewModelProvider.get(EventsViewModel.class);

        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.eventsRecyclerView.setNestedScrollingEnabled(false);

        binding.listingsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.listingsRecyclerView.setNestedScrollingEnabled(false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventsViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            binding.eventsRecyclerView.setAdapter(new EventAdapter(events));
        });

        listingsViewModel.getListings().observe(getViewLifecycleOwner(), listings -> {
            binding.listingsRecyclerView.setAdapter(new ListingAdapter(listings));
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
