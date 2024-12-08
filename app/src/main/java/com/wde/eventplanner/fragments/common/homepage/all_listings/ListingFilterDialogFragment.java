package com.wde.eventplanner.fragments.common.homepage.all_listings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wde.eventplanner.adapters.DropdownArrayAdapter;
import com.wde.eventplanner.databinding.DialogListingFilterBinding;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListingFilterDialogFragment extends DialogFragment {
    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayList<ListingCategoryDTO> categories;
    private DialogListingFilterBinding binding;
    private final AllListingsFragment parent;

    public ListingFilterDialogFragment(AllListingsFragment fragment) {
        this.parent = fragment;
    }

    @Nullable
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogListingFilterBinding.inflate(inflater, container, false);
        ListingCategoriesViewModel viewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener(this::onTouchOutsideDialog);
        }

        binding.categoryDropdown.setOnClickListener(v -> binding.categoryDropdown.showDropDown());
        binding.categoryDropdown.setOnFocusChangeListener((v, hasFocus) -> onDropdownFocusChanged(hasFocus, binding.categoryDropdown, categoryNames));

        binding.typeDropdown.setAdapter(new DropdownArrayAdapter(requireActivity(), List.of("All listings", "Products", "Services")));
        binding.typeDropdown.setOnClickListener(v -> binding.typeDropdown.showDropDown());
        binding.typeDropdown.setOnItemClickListener(this::onTypeDropdownItemClick);
        binding.typeDropdown.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) binding.typeDropdown.showDropDown();
        });

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.filterButton.setOnClickListener(this::onFilterButtonClicked);

        viewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), this::onCategoriesChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });
        viewModel.fetchActiveListingCategories();

        return binding.getRoot();
    }

    private boolean onTouchOutsideDialog(View v, MotionEvent event) {
        Rect rect = new Rect();
        binding.categoryDropdown.getGlobalVisibleRect(rect);
        if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            binding.categoryDropdown.clearFocus();
            InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.categoryDropdown.getWindowToken(), 0);
        }
        binding.typeDropdown.getGlobalVisibleRect(rect);
        if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            binding.typeDropdown.clearFocus();
            InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.typeDropdown.getWindowToken(), 0);
        }
        return false;
    }

    private void onDropdownFocusChanged(boolean hasFocus, MaterialAutoCompleteTextView dropdown, ArrayList<String> values) {
        String input = dropdown.getText().toString().trim();
        if (hasFocus) dropdown.showDropDown();
        else {
            Optional<String> found = values.stream().filter(value -> value.equalsIgnoreCase(input)).findFirst();
            dropdown.setText(found.orElse(""));
        }
    }

    private void onTypeDropdownItemClick(AdapterView<?> parent, View v, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        ListingType type = selected.equals("Products") ? ListingType.PRODUCT : (selected.equals("Services") ? ListingType.SERVICE : null);
        ArrayList<String> categories = this.categories != null ? this.categories.stream().filter(dto -> dto.getListingType() == type || type == null)
                .map(ListingCategoryDTO::getName).collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>();
        binding.categoryDropdown.setAdapter(new DropdownArrayAdapter(requireActivity(), categories));
    }

    private void onFilterButtonClicked(View v) {
        String categoryInput = binding.categoryDropdown.getText().toString().trim();
        String category = categories.stream().filter(dto -> dto.getName().equalsIgnoreCase(categoryInput)).findFirst().map(ListingCategoryDTO::getId).orElse(null);
        String typeInput = binding.typeDropdown.getText().toString().trim();
        String type = (typeInput.equals("Products") ? "PRODUCT" : (typeInput.equals("Services") ? "SERVICE" : null));

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

    private void onCategoriesChanged(ArrayList<ListingCategoryDTO> categories) {
        this.categories = categories;
        String selectedType = binding.typeDropdown.getText().toString();
        ListingType type = selectedType.equals("Products") ? ListingType.PRODUCT : (selectedType.equals("Services") ? ListingType.SERVICE : null);
        categoryNames = categories != null ? categories.stream().filter(dto -> dto.getListingType() == type || type == null)
                .map(ListingCategoryDTO::getName).collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>();
        binding.categoryDropdown.setAdapter(new DropdownArrayAdapter(requireActivity(), categoryNames));
    }
}
