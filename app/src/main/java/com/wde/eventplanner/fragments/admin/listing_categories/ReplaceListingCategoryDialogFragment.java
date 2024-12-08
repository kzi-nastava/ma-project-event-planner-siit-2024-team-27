package com.wde.eventplanner.fragments.admin.listing_categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.databinding.DialogReplaceListingCategoryBinding;
import com.wde.eventplanner.fragments.common.CustomDropDown;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;

public class ReplaceListingCategoryDialogFragment extends DialogFragment {
    private DialogReplaceListingCategoryBinding binding;
    private final ListingCategory toBeReplacedCategory;
    private ListingCategoriesViewModel viewModel;

    public ReplaceListingCategoryDialogFragment(ListingCategory toBeReplacedCategory) {
        this.toBeReplacedCategory = toBeReplacedCategory;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogReplaceListingCategoryBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener(binding.categoryDropdown::onTouchOutsideDropDown);
        }

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.replaceButton.setOnClickListener(v -> replaceListing());

        viewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), this::onCategoriesChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });
        viewModel.fetchActiveListingCategories();

        return binding.getRoot();
    }

    private void onCategoriesChanged(ArrayList<ListingCategory> categories) {
        @SuppressWarnings("unchecked")
        CustomDropDown<ListingCategory> categoryDropdown = binding.categoryDropdown;
        ListingType type = toBeReplacedCategory.getListingType();
        categoryDropdown.onValuesChanged(categories, ListingCategory::getName, category -> category.getListingType() == type || type == null);
    }

    private void replaceListing() {
        ListingCategory category = (ListingCategory) binding.categoryDropdown.getSelected();
        if (category != null)
            viewModel.replacePendingListingCategory(new ReplacingListingCategory(toBeReplacedCategory.getId(), category.getId(), toBeReplacedCategory.getListingType()));
    }
}
