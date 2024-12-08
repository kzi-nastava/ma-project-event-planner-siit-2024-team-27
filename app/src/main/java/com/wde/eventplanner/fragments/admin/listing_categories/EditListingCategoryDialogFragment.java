package com.wde.eventplanner.fragments.admin.listing_categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.databinding.DialogEditListingCategoryBinding;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

public class EditListingCategoryDialogFragment extends DialogFragment {
    private DialogEditListingCategoryBinding binding;
    private final ListingCategory listingCategory;
    private ListingCategoriesViewModel viewModel;
    private final boolean isCategoryActive;

    public EditListingCategoryDialogFragment(ListingCategory listingCategory, boolean isCategoryActive) {
        this.isCategoryActive = isCategoryActive;
        this.listingCategory = listingCategory;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditListingCategoryBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        binding.name.setText(listingCategory.getName());
        binding.description.setText(listingCategory.getDescription());
        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.editListingCategoryButton.setOnClickListener(v -> editListing());

        return binding.getRoot();
    }

    private void editListing() {
        if (binding.name.getText() != null && binding.description.getText() != null) {
            String name = binding.name.getText().toString();
            String description = binding.description.getText().toString();
            ListingCategory dto = new ListingCategory(listingCategory.getId(), name, listingCategory.getIsPending(),
                    description, listingCategory.getIsDeleted(), listingCategory.getListingType());

            if (isCategoryActive)
                viewModel.editActiveListingCategory(dto.getId(), dto);
            else
                viewModel.editPendingListingCategory(dto.getId(), dto);
        }
        dismiss();
    }
}
