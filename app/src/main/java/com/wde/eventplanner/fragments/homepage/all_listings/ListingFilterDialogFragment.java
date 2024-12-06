package com.wde.eventplanner.fragments.homepage.all_listings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.databinding.DialogListingFilterBinding;
import com.wde.eventplanner.fragments.AdminListingCategories.AdminListingCategoriesViewModel;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ListingType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ListingFilterDialogFragment extends DialogFragment {
    private ArrayList<ListingCategoryDTO> categories;
    private String[] categoryNames = new String[]{};
    private DialogListingFilterBinding binding;
    private final AllListingsFragment parent;

    public ListingFilterDialogFragment(AllListingsFragment fragment) {
        this.parent = fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = android.view.Gravity.CENTER;
        dialog.getWindow().setAttributes(params);

        return dialog;
    }

    @Nullable
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogListingFilterBinding.inflate(inflater, container, false);

        binding.categoryDropdown.setOnClickListener(v -> binding.categoryDropdown.showDropDown());
        binding.categoryDropdown.setOnFocusChangeListener((v, hasFocus) -> {
            String input = binding.categoryDropdown.getText().toString().trim();
            if (hasFocus) binding.categoryDropdown.showDropDown();
            else {
                Optional<String> found = Arrays.stream(categoryNames).filter(category -> category.equalsIgnoreCase(input)).findFirst();
                binding.categoryDropdown.setText(found.orElse(""));
            }
        });

        binding.typeDropdown.setAdapter(getTypeArrayAdapter());
        binding.typeDropdown.setOnClickListener(v -> binding.typeDropdown.showDropDown());
        binding.typeDropdown.setOnItemClickListener((parent, v, position, id) -> {
            String selected = parent.getItemAtPosition(position).toString();
            ListingType type = selected.equals("Products") ? ListingType.PRODUCT : (selected.equals("Services") ? ListingType.SERVICE : null);
            String[] categories = this.categories != null ? this.categories.stream().filter(dto -> dto.getListingType() == type || type == null)
                    .map(ListingCategoryDTO::getName).toArray(String[]::new) : new String[]{};
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
            binding.categoryDropdown.setAdapter(categoryAdapter);
        });
        binding.typeDropdown.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) binding.typeDropdown.showDropDown();
        });

        binding.getRoot().setOnTouchListener((v, event) -> {
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
        });

        binding.categoryDropdown.clearFocus();
        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.filterButton.setOnClickListener(v -> {
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
        });

        return binding.getRoot();
    }

    @NonNull
    private ArrayAdapter<String> getTypeArrayAdapter() {
        String[] types = new String[]{"All listings", "Products", "Services"};
        return new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, types) {
            @NonNull
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        results.count = types.length;
                        results.values = types;
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        notifyDataSetChanged();
                    }
                };
            }
        };
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdminListingCategoriesViewModel listingCategoriesViewModel = new ViewModelProvider(this).get(AdminListingCategoriesViewModel.class);

        listingCategoriesViewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), categories -> {
            this.categories = categories;
            String selectedType = binding.typeDropdown.getText().toString();
            ListingType type = selectedType.equals("Products") ? ListingType.PRODUCT : (selectedType.equals("Services") ? ListingType.SERVICE : null);
            categoryNames = categories != null ? categories.stream().filter(dto -> dto.getListingType() == type || type == null)
                    .map(ListingCategoryDTO::getName).toArray(String[]::new) : new String[]{};
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryNames);
            binding.categoryDropdown.setAdapter(categoryAdapter);

        });

        listingCategoriesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        listingCategoriesViewModel.fetchActiveListingCategories();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        binding.categoryDropdown.clearFocus();
        super.onDismiss(dialog);
    }
}
