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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.AdminListingCategoryActiveListAdapter;
import com.wde.eventplanner.adapters.AdminListingCategoryPendingListAdapter;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategoryDTO;

public class AdminListingCategoriesFragment extends Fragment {
    private AdminListingCategoryPendingListAdapter pendingListAdapter;
    private AdminListingCategoryActiveListAdapter activeListAdapter;
    private RecyclerView pendingListingCategoriesRecyclerView;
    private RecyclerView activeListingCategoriesRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_categories_screen, container, false);

        MaterialButton createListingButton = view.findViewById(R.id.createListingButton);

        CreateListingCategoryDialogFragment createDialog = new CreateListingCategoryDialogFragment(this);
        createListingButton.setOnClickListener(v -> createDialog.show(getParentFragmentManager(), "createDialog"));

        pendingListingCategoriesRecyclerView = view.findViewById(R.id.pendingListingCategoriesRecyclerView);
        pendingListingCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        pendingListingCategoriesRecyclerView.setNestedScrollingEnabled(false);

        activeListingCategoriesRecyclerView = view.findViewById(R.id.activeListingCategoriesRecyclerView);
        activeListingCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        activeListingCategoriesRecyclerView.setNestedScrollingEnabled(false);

        return view;
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
            this.activeListingCategoriesRecyclerView.setAdapter(new AdminListingCategoryActiveListAdapter(activeCategories, this));
            viewModel.fetchPendingListingCategories();
            viewModel.getPendingListingCategories().observe(getViewLifecycleOwner(), pendingCategories ->
                    this.pendingListingCategoriesRecyclerView.setAdapter(new AdminListingCategoryPendingListAdapter(pendingCategories, activeCategories, this)));
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
