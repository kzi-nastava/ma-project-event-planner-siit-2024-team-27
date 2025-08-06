package com.wde.eventplanner.fragments.guest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.EventAdapter;
import com.wde.eventplanner.databinding.FragmentFavouriteEventsBinding;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.GuestsViewModel;

public class FavouriteEventsFragment extends Fragment {
    private FragmentFavouriteEventsBinding binding;
    private GuestsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouriteEventsBinding.inflate(inflater, container, false);
        binding.favouriteRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.favouriteRecyclerView.setNestedScrollingEnabled(false);

        viewModel = new ViewModelProvider(requireActivity()).get(GuestsViewModel.class);
        viewModel.getFavouriteEvents(TokenManager.getUserId(requireContext())).observe(getViewLifecycleOwner(), events ->
                binding.favouriteRecyclerView.setAdapter(new EventAdapter(events, NavHostFragment.findNavController(this)))
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
