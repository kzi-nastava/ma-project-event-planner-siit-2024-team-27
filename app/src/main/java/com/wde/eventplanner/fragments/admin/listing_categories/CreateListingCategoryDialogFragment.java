package com.wde.eventplanner.fragments.admin.listing_categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.databinding.DialogCreateListingCategoryBinding;
import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateListingCategoryDialogFragment extends DialogFragment {
    private DialogCreateListingCategoryBinding binding;
    private ListingCategoriesViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCreateListingCategoryBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener(binding.typeDropdown::onTouchOutsideDropDown);
        }

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.createButton.setOnClickListener(v -> createListing());
        binding.typeDropdown.disableAutoComplete();

        @SuppressWarnings("unchecked")
        CustomDropDown<ListingType> typeDropdown = binding.typeDropdown;
        typeDropdown.changeValues(new ArrayList<>(Arrays.asList(ListingType.values())), ListingType::toString);

        return binding.getRoot();
    }

    private void createListing() {
        if (binding.name.getText() != null && binding.description.getText() != null) {
            String name = binding.name.getText().toString();
            String description = binding.description.getText().toString();
            ListingType type = (ListingType) binding.typeDropdown.getSelected();
            viewModel.createActiveListingCategory(new ListingCategory(name, description, type));
        }
        dismiss();
    }
}
