package com.wde.eventplanner.fragments.admin.listing_categories;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.adapters.DropdownArrayAdapter;
import com.wde.eventplanner.databinding.DialogCreateListingCategoryBinding;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

public class CreateListingCategoryDialogFragment extends DialogFragment {
    private DialogCreateListingCategoryBinding binding;
    private ListingCategoriesViewModel viewModel;
    private ListingType selectedListingType;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCreateListingCategoryBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener(this::onTouchOutsideDialog);
        }

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.createButton.setOnClickListener(v -> createListing());

        binding.typeDropdown.setAdapter(new DropdownArrayAdapter<>(requireActivity(), ListingType.values()));
        binding.typeDropdown.setOnClickListener(v -> binding.typeDropdown.showDropDown());
        binding.typeDropdown.setOnItemClickListener((parent, v, position, id) -> {
            selectedListingType = (ListingType) parent.getItemAtPosition(position);
        });
        binding.typeDropdown.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) binding.typeDropdown.showDropDown();
            InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.typeDropdown.getWindowToken(), 0);
        });

        return binding.getRoot();
    }

    private boolean onTouchOutsideDialog(View v, MotionEvent event) {
        Rect rect = new Rect();
        binding.typeDropdown.getGlobalVisibleRect(rect);
        if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            binding.typeDropdown.clearFocus();
            InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.typeDropdown.getWindowToken(), 0);
        }
        return false;
    }

    private void createListing() {
        if (binding.name.getText() != null && binding.description.getText() != null) {
            String name = binding.name.getText().toString();
            String description = binding.description.getText().toString();
            viewModel.createActiveListingCategory(new ListingCategory(name, description, selectedListingType));
        }
        dismiss();
    }
}
