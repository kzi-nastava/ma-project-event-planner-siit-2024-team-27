package com.wde.eventplanner.fragments.common.homepage.all_listings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.components.SingleToast;
import com.wde.eventplanner.databinding.DialogListingFilterBinding;
import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListingFilterDialogFragment extends DialogFragment {
    public interface ListingsFilterListener {
        void onFilterPressed(String type, String category, String minPrice, String maxPrice, String minRating, String maxRating);
    }

    private final ListingsFilterListener parent;
    private DialogListingFilterBinding binding;

    public ListingFilterDialogFragment(ListingsFilterListener fragment) {
        this.parent = fragment;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogListingFilterBinding.inflate(inflater, container, false);
        ListingCategoriesViewModel viewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener((v, event) -> {
                binding.categoryDropdown.onTouchOutsideDropDown(v, event);
                binding.typeDropdown.onTouchOutsideDropDown(v, event);
                v.performClick();
                return false;
            });
        }

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.filterButton.setOnClickListener(this::onFilterButtonClicked);
        binding.typeDropdown.disableAutoComplete();

        @SuppressWarnings("unchecked")
        CustomDropDown<ListingType> typeDropdown = binding.typeDropdown;
        typeDropdown.setItems(new ArrayList<>(List.of(
                new CustomDropDown.CustomDropDownItem<>("All listings", null),
                new CustomDropDown.CustomDropDownItem<>("Products", ListingType.PRODUCT),
                new CustomDropDown.CustomDropDownItem<>("Services", ListingType.SERVICE))));
        typeDropdown.setOnDropdownItemClickListener(this::onTypeDropdownItemClick);

        viewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), this::onCategoriesChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                viewModel.clearErrorMessage();
            }
        });
        viewModel.fetchActiveListingCategories();

        return binding.getRoot();
    }

    private void onTypeDropdownItemClick(ListingType type) {
        @SuppressWarnings("unchecked")
        CustomDropDown<ListingCategory> categoryDropdown = binding.categoryDropdown;
        categoryDropdown.changeFilter(category -> category.getListingType() == type || type == null);
    }

    private void onCategoriesChanged(ArrayList<ListingCategory> categories) {
        @SuppressWarnings("unchecked")
        CustomDropDown<ListingCategory> categoryDropdown = binding.categoryDropdown;
        ListingType type = (ListingType) binding.typeDropdown.getSelected();
        categoryDropdown.changeValues(categories, ListingCategory::getName, category -> category.getListingType() == type || type == null);
    }

    private void onFilterButtonClicked(View v) {
        String category = binding.categoryDropdown.getSelected() == null ? null : ((ListingCategory) binding.categoryDropdown.getSelected()).getId();
        String type = binding.typeDropdown.getSelected() == null ? null : binding.typeDropdown.getSelected().toString().toUpperCase();

        String minPrice, maxPrice, minRating, maxRating;
        if (binding.minPrice.getText() != null && !binding.minPrice.getText().toString().isBlank())
            minPrice = binding.minPrice.getText().toString();
        else minPrice = null;
        if (binding.maxPrice.getText() != null && !binding.maxPrice.getText().toString().isBlank())
            maxPrice = binding.maxPrice.getText().toString();
        else maxPrice = null;
        if (binding.minRating.getText() != null && !binding.minRating.getText().toString().isBlank())
            minRating = binding.minRating.getText().toString();
        else minRating = null;
        if (binding.maxRating.getText() != null && !binding.maxRating.getText().toString().isBlank())
            maxRating = binding.maxRating.getText().toString();
        else maxRating = null;

        parent.onFilterPressed(type, category, minPrice, maxPrice, minRating, maxRating);
        dismiss();
    }
}
