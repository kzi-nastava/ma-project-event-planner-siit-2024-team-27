package com.wde.eventplanner.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.databinding.FragmentFavouriteListingsBinding;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.EventOrganizerViewModel;

public class FavouriteListingsFragment extends Fragment {
    private FragmentFavouriteListingsBinding binding;
    private EventOrganizerViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouriteListingsBinding.inflate(inflater, container, false);
        binding.favouriteRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.favouriteRecyclerView.setNestedScrollingEnabled(false);

        viewModel = new ViewModelProvider(requireActivity()).get(EventOrganizerViewModel.class);
        viewModel.getFavouriteListings(TokenManager.getUserId(requireContext())).observe(getViewLifecycleOwner(), listings ->
                binding.favouriteRecyclerView.setAdapter(new ListingAdapter(listings, NavHostFragment.findNavController(this)))
        );

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                viewModel.clearErrorMessage();
            }
        });

        return binding.getRoot();
    }
}
