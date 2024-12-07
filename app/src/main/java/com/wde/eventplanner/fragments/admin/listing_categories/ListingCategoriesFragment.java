package com.wde.eventplanner.fragments.admin.listing_categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.ListingCategoryActiveListAdapter;
import com.wde.eventplanner.adapters.ListingCategoryPendingListAdapter;
import com.wde.eventplanner.databinding.FragmentListingCategoriesBinding;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategoryDTO;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

public class ListingCategoriesFragment extends Fragment {
    private FragmentListingCategoriesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListingCategoriesBinding.inflate(inflater, container, false);

        CreateListingCategoryDialogFragment createDialog = new CreateListingCategoryDialogFragment(this);

        binding.createListingButton.setOnClickListener(v -> createDialog.show(getParentFragmentManager(), "createDialog"));

        binding.pendingListingCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.pendingListingCategoriesRecyclerView.setNestedScrollingEnabled(false);

        binding.activeListingCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.activeListingCategoriesRecyclerView.setNestedScrollingEnabled(false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(ListingCategoriesViewModel.class);

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), activeCategories -> {
            binding.activeListingCategoriesRecyclerView.setAdapter(new ListingCategoryActiveListAdapter(activeCategories, this));
            viewModel.fetchPendingListingCategories();
            viewModel.getPendingListingCategories().observe(getViewLifecycleOwner(), pendingCategories ->
                    binding.pendingListingCategoriesRecyclerView.setAdapter(new ListingCategoryPendingListAdapter(pendingCategories, activeCategories, this)));
        });

        viewModel.fetchActiveListingCategories();
    }

    public void createActiveListing(ListingCategoryDTO listingCategoryDTO) {
        ListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(ListingCategoriesViewModel.class);
        viewModel.createActiveListingCategory(listingCategoryDTO);
    }

    public void editActiveListing(ListingCategoryDTO listingCategoryDTO) {
        ListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(ListingCategoriesViewModel.class);
        viewModel.editActiveListingCategory(listingCategoryDTO.getId(), listingCategoryDTO);
    }

    public void editPendingListing(ListingCategoryDTO listingCategoryDTO) {
        ListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(ListingCategoriesViewModel.class);
        viewModel.editPendingListingCategory(listingCategoryDTO.getId(), listingCategoryDTO);
    }

    public void deleteActiveListing(ListingCategoryDTO listingCategoryDTO) {
        ListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(ListingCategoriesViewModel.class);
        viewModel.deleteActiveListingCategory(listingCategoryDTO);
    }

    public void approvePendingListing(ListingCategoryDTO listingCategoryDTO) {
        ListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(ListingCategoriesViewModel.class);
        viewModel.approvePendingListingCategory(listingCategoryDTO);
    }

    public void replacePendingListing(ReplacingListingCategoryDTO replacingListingCategoryDTO) {
        ListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(ListingCategoriesViewModel.class);
        viewModel.replacePendingListingCategory(replacingListingCategoryDTO);
    }
}
