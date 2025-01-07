package com.wde.eventplanner.fragments.admin.reviews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.PendingEventReviewAdapter;
import com.wde.eventplanner.adapters.PendingListingReviewAdapter;
import com.wde.eventplanner.databinding.FragmentReviewsBinding;
import com.wde.eventplanner.models.reviews.EventReviewResponse;
import com.wde.eventplanner.models.reviews.ListingReviewResponse;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.viewmodels.EventReviewsViewModel;
import com.wde.eventplanner.viewmodels.ListingReviewsViewModel;

import java.util.ArrayList;

public class ReviewsFragment extends Fragment {
    private FragmentReviewsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        ListingReviewsViewModel listingReviewsViewModel = new ViewModelProvider(requireActivity()).get(ListingReviewsViewModel.class);
        EventReviewsViewModel eventReviewsViewModel = new ViewModelProvider(requireActivity()).get(EventReviewsViewModel.class);

        binding.listingReviews.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.listingReviews.setNestedScrollingEnabled(false);
        binding.listingReviews.setAdapter(listingReviewsViewModel.getPendingReviews().isInitialized() ?
                new PendingListingReviewAdapter(listingReviewsViewModel.getPendingReviews().getValue(), this, NavHostFragment.findNavController(this)) :
                new PendingListingReviewAdapter(this, NavHostFragment.findNavController(this)));

        binding.eventReviews.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.eventReviews.setNestedScrollingEnabled(false);
        binding.eventReviews.setAdapter(eventReviewsViewModel.getPendingReviews().isInitialized() ?
                new PendingEventReviewAdapter(eventReviewsViewModel.getPendingReviews().getValue(), this, NavHostFragment.findNavController(this)) :
                new PendingEventReviewAdapter(this, NavHostFragment.findNavController(this)));

        listingReviewsViewModel.getPendingReviews().observe(getViewLifecycleOwner(), this::OnListingReviewsChanged);
        listingReviewsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                listingReviewsViewModel.clearErrorMessage();
            }
        });

        eventReviewsViewModel.getPendingReviews().observe(getViewLifecycleOwner(), this::OnEventReviewsChanged);
        eventReviewsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                eventReviewsViewModel.clearErrorMessage();
            }
        });

        listingReviewsViewModel.fetchPendingReviews();
        eventReviewsViewModel.fetchPendingReviews();

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void OnListingReviewsChanged(ArrayList<ListingReviewResponse> listingReviews) {
        if (binding.listingReviews.getAdapter() != null) {
            PendingListingReviewAdapter adapter = (PendingListingReviewAdapter) binding.listingReviews.getAdapter();
            ArrayList<ListingReviewResponse> reviews = new ArrayList<>(listingReviews);
            adapter.reviews.clear();
            adapter.reviews.addAll(reviews);
            adapter.notifyDataSetChanged();
        }
        binding.listingReviewsTitle.setVisibility(listingReviews.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void OnEventReviewsChanged(ArrayList<EventReviewResponse> eventReviews) {
        if (binding.eventReviews.getAdapter() != null) {
            PendingEventReviewAdapter adapter = (PendingEventReviewAdapter) binding.eventReviews.getAdapter();
            ArrayList<EventReviewResponse> reviews = new ArrayList<>(eventReviews);
            adapter.reviews.clear();
            adapter.reviews.addAll(reviews);
            adapter.notifyDataSetChanged();
        }
        binding.eventReviewsTitle.setVisibility(eventReviews.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
