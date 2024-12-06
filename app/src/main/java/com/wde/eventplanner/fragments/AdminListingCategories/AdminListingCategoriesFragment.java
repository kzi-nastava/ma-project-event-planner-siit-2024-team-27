package com.wde.eventplanner.fragments.AdminListingCategories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.AdminListingCategoryActiveListAdapter;
import com.wde.eventplanner.adapters.AdminListingCategoryPendingListAdapter;
import com.wde.eventplanner.databinding.FragmentListingCategoriesBinding;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategoryDTO;

public class AdminListingCategoriesFragment extends Fragment {
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
        AdminListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(AdminListingCategoriesViewModel.class);

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), activeCategories -> {
            binding.activeListingCategoriesRecyclerView.setAdapter(new AdminListingCategoryActiveListAdapter(activeCategories, this));
            viewModel.fetchPendingListingCategories();
            viewModel.getPendingListingCategories().observe(getViewLifecycleOwner(), pendingCategories ->
                    binding.pendingListingCategoriesRecyclerView.setAdapter(new AdminListingCategoryPendingListAdapter(pendingCategories, activeCategories, this)));
        });

        viewModel.fetchActiveListingCategories();
    }

    public void createActiveListing(ListingCategoryDTO listingCategoryDTO) {
        AdminListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(AdminListingCategoriesViewModel.class);
        viewModel.createActiveListingCategory(listingCategoryDTO);
    }

    public void editActiveListing(ListingCategoryDTO listingCategoryDTO) {
        AdminListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(AdminListingCategoriesViewModel.class);
        viewModel.editActiveListingCategory(listingCategoryDTO.getId(), listingCategoryDTO);
    }

    public void editPendingListing(ListingCategoryDTO listingCategoryDTO) {
        AdminListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(AdminListingCategoriesViewModel.class);
        viewModel.editPendingListingCategory(listingCategoryDTO.getId(), listingCategoryDTO);
    }

    public void deleteActiveListing(ListingCategoryDTO listingCategoryDTO) {
        AdminListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(AdminListingCategoriesViewModel.class);
        viewModel.deleteActiveListingCategory(listingCategoryDTO);
    }

    public void approvePendingListing(ListingCategoryDTO listingCategoryDTO) {
        AdminListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(AdminListingCategoriesViewModel.class);
        viewModel.approvePendingListingCategory(listingCategoryDTO);
    }

    public void replacePendingListing(ReplacingListingCategoryDTO replacingListingCategoryDTO) {
        AdminListingCategoriesViewModel viewModel = new ViewModelProvider(this).get(AdminListingCategoriesViewModel.class);
        viewModel.replacePendingListingCategory(replacingListingCategoryDTO);
    }
}
