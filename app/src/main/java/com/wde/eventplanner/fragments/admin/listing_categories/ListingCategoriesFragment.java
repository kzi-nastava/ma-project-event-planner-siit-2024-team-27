package com.wde.eventplanner.fragments.admin.listing_categories;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.ListingCategoryActiveAdapter;
import com.wde.eventplanner.adapters.ListingCategoryPendingAdapter;
import com.wde.eventplanner.components.SingleToast;
import com.wde.eventplanner.databinding.FragmentListingCategoriesBinding;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;

public class ListingCategoriesFragment extends Fragment {
    private FragmentListingCategoriesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListingCategoriesBinding.inflate(inflater, container, false);
        ListingCategoriesViewModel viewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);

        CreateListingCategoryDialogFragment createDialog = new CreateListingCategoryDialogFragment();
        binding.createListingButton.setOnClickListener(v -> createDialog.show(getParentFragmentManager(), "createDialog"));

        binding.pendingListingCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.pendingListingCategoriesRecyclerView.setNestedScrollingEnabled(false);
        binding.pendingListingCategoriesRecyclerView.setAdapter(viewModel.getPendingListingCategories().isInitialized() ?
                new ListingCategoryPendingAdapter(viewModel.getPendingListingCategories().getValue(), this) : new ListingCategoryPendingAdapter(this));

        binding.activeListingCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.activeListingCategoriesRecyclerView.setNestedScrollingEnabled(false);
        binding.activeListingCategoriesRecyclerView.setAdapter(viewModel.getActiveListingCategories().isInitialized() ?
                new ListingCategoryActiveAdapter(viewModel.getActiveListingCategories().getValue(), this) : new ListingCategoryActiveAdapter(this));

        viewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), this::OnActiveCategoriesChanged);
        viewModel.getPendingListingCategories().observe(getViewLifecycleOwner(), this::OnPendingCategoriesChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                viewModel.clearErrorMessage();
            }
        });

        viewModel.fetchPendingListingCategories();
        viewModel.fetchActiveListingCategories();

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void OnActiveCategoriesChanged(ArrayList<ListingCategory> activeCategories) {
        if (binding.activeListingCategoriesRecyclerView.getAdapter() != null) {
            ListingCategoryActiveAdapter adapter = (ListingCategoryActiveAdapter) binding.activeListingCategoriesRecyclerView.getAdapter();
            ArrayList<ListingCategory> activeCategoriesTmp = new ArrayList<>(activeCategories);
            adapter.activeListingCategories.clear();
            adapter.activeListingCategories.addAll(activeCategoriesTmp);
            adapter.notifyDataSetChanged();
        }
        binding.activeListingCategoriesTitle.setVisibility(activeCategories.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void OnPendingCategoriesChanged(ArrayList<ListingCategory> pendingCategories) {
        if (binding.pendingListingCategoriesRecyclerView.getAdapter() != null) {
            ListingCategoryPendingAdapter adapter = (ListingCategoryPendingAdapter) binding.pendingListingCategoriesRecyclerView.getAdapter();
            ArrayList<ListingCategory> pendingCategoriesTmp = new ArrayList<>(pendingCategories);
            adapter.pendingListingCategories.clear();
            adapter.pendingListingCategories.addAll(pendingCategoriesTmp);
            adapter.notifyDataSetChanged();
        }
        binding.pendingListingCategoriesTitle.setVisibility(pendingCategories.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
