package com.wde.eventplanner.fragments.admin.listing_categories;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wde.eventplanner.databinding.DialogReplaceListingCategoryBinding;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReplaceListingCategoryDialogFragment extends DialogFragment {
    private ArrayList<String> categoryNames = new ArrayList<>();
    private DialogReplaceListingCategoryBinding binding;
    private final ListingCategory toBeReplacedCategory;
    private ArrayList<ListingCategory> categories;
    private ListingCategoriesViewModel viewModel;

    public ReplaceListingCategoryDialogFragment(ListingCategory toBeReplacedCategory) {
        this.toBeReplacedCategory = toBeReplacedCategory;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogReplaceListingCategoryBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener(this::onTouchOutsideDialog);
        }

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.replaceButton.setOnClickListener(v -> replaceListing());

        binding.categoryDropdown.setOnClickListener(v -> binding.categoryDropdown.showDropDown());
        binding.categoryDropdown.setOnFocusChangeListener((v, hasFocus) -> onDropdownFocusChanged(hasFocus, binding.categoryDropdown, categoryNames));

        viewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), this::onCategoriesChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });
        viewModel.fetchActiveListingCategories();

        return binding.getRoot();
    }

    private void onDropdownFocusChanged(boolean hasFocus, MaterialAutoCompleteTextView dropdown, ArrayList<String> values) {
        String input = dropdown.getText().toString().trim();
        if (hasFocus) {
            dropdown.setText("");
            dropdown.showDropDown();
        } else {
            Optional<String> found = values.stream().filter(value -> value.equalsIgnoreCase(input)).findFirst();
            dropdown.setText(found.orElse(""));
        }
    }

    private boolean onTouchOutsideDialog(View v, MotionEvent event) {
        Rect rect = new Rect();
        binding.categoryDropdown.getGlobalVisibleRect(rect);
        if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            binding.categoryDropdown.clearFocus();
            InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.categoryDropdown.getWindowToken(), 0);
        }
        return false;
    }

    private void onCategoriesChanged(ArrayList<ListingCategory> categories) {
        this.categories = categories;
        ListingType type = toBeReplacedCategory.getListingType();
        categoryNames = categories != null ? categories.stream().filter(dto -> dto.getListingType() == type || type == null)
                .map(ListingCategory::getName).collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>();
        binding.categoryDropdown.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, categoryNames));
    }

    private void replaceListing() {
        String categoryInput = binding.categoryDropdown.getText().toString().trim();
        if (categories != null)
            categories.stream().filter(dto -> dto.getName().equalsIgnoreCase(categoryInput)).findFirst().map(ListingCategory::getId).ifPresent(categoryId ->
                    viewModel.replacePendingListingCategory(new ReplacingListingCategory(toBeReplacedCategory.getId(), categoryId, toBeReplacedCategory.getListingType())));
        dismiss();
    }
}
