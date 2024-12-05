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
import android.widget.Button;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.R;
import com.wde.eventplanner.fragments.AdminListingCategories.AdminListingCategoriesViewModel;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ListingType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ListingFilterDialogFragment extends DialogFragment {
    private MaterialAutoCompleteTextView categoryAutoCompleteTextView;
    private MaterialAutoCompleteTextView typeAutoCompleteTextView;
    private ArrayList<ListingCategoryDTO> categories;
    private String[] categoryNames = new String[]{};
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
        View view = inflater.inflate(R.layout.fragment_listing_filter_dialog, null);

        categoryAutoCompleteTextView = view.findViewById(R.id.categoryDropdown);
        categoryAutoCompleteTextView.setOnClickListener(v -> categoryAutoCompleteTextView.showDropDown());
        categoryAutoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
            String input = categoryAutoCompleteTextView.getText().toString().trim();
            if (hasFocus) categoryAutoCompleteTextView.showDropDown();
            else {
                Optional<String> found = Arrays.stream(categoryNames).filter(category -> category.equalsIgnoreCase(input)).findFirst();
                categoryAutoCompleteTextView.setText(found.orElse(""));
            }
        });

        typeAutoCompleteTextView = view.findViewById(R.id.typeDropdown);
        typeAutoCompleteTextView.setAdapter(getTypeArrayAdapter());
        typeAutoCompleteTextView.setOnClickListener(v -> typeAutoCompleteTextView.showDropDown());
        typeAutoCompleteTextView.setOnItemClickListener((parent, v, position, id) -> {
            String selected = parent.getItemAtPosition(position).toString();
            ListingType type = selected.equals("Products") ? ListingType.PRODUCT : (selected.equals("Services") ? ListingType.SERVICE : null);
            String[] categories = this.categories != null ? this.categories.stream().filter(dto -> dto.getListingType() == type || type == null)
                    .map(ListingCategoryDTO::getName).toArray(String[]::new) : new String[]{};
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
            categoryAutoCompleteTextView.setAdapter(categoryAdapter);
        });
        typeAutoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) typeAutoCompleteTextView.showDropDown();
        });

        view.setOnTouchListener((v, event) -> {
            Rect rect = new Rect();
            categoryAutoCompleteTextView.getGlobalVisibleRect(rect);
            if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                categoryAutoCompleteTextView.clearFocus();
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(categoryAutoCompleteTextView.getWindowToken(), 0);
            }
            typeAutoCompleteTextView.getGlobalVisibleRect(rect);
            if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                typeAutoCompleteTextView.clearFocus();
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(typeAutoCompleteTextView.getWindowToken(), 0);
            }
            return false;
        });

        Button closeButton = view.findViewById(R.id.closeButton);
        categoryAutoCompleteTextView.clearFocus();
        closeButton.setOnClickListener(v -> dismiss());

        TextInputEditText minPriceInput = view.findViewById(R.id.minPrice);
        TextInputEditText maxPriceInput = view.findViewById(R.id.maxPrice);
        TextInputEditText minRatingInput = view.findViewById(R.id.minRating);
        TextInputEditText maxRatingInput = view.findViewById(R.id.maxRating);

        Button filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> {
            String categoryInput = categoryAutoCompleteTextView.getText().toString().trim();
            String category = categories.stream().filter(dto -> dto.getName().equalsIgnoreCase(categoryInput)).findFirst().map(ListingCategoryDTO::getId).orElse(null);
            String typeInput = typeAutoCompleteTextView.getText().toString().trim();
            String type = (typeInput.equals("Products") ? "PRODUCT" : (typeInput.equals("Services") ? "SERVICE" : null));

            String minPrice, maxPrice, minRating, maxRating;
            if (minPriceInput.getText() != null && !minPriceInput.getText().toString().isBlank())
                minPrice = minPriceInput.getText().toString();
            else minPrice = null;
            if (maxPriceInput.getText() != null && !maxPriceInput.getText().toString().isBlank())
                maxPrice = maxPriceInput.getText().toString();
            else maxPrice = null;
            if (minRatingInput.getText() != null && !minRatingInput.getText().toString().isBlank())
                minRating = minRatingInput.getText().toString();
            else minRating = null;
            if (maxRatingInput.getText() != null && !maxRatingInput.getText().toString().isBlank())
                maxRating = maxRatingInput.getText().toString();
            else maxRating = null;

            parent.onFilterPressed(type, category, minPrice, maxPrice, minRating, maxRating);
            dismiss();
        });

        return view;
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
            String selectedType = typeAutoCompleteTextView.getText().toString();
            ListingType type = selectedType.equals("Products") ? ListingType.PRODUCT : (selectedType.equals("Services") ? ListingType.SERVICE : null);
            categoryNames = categories != null ? categories.stream().filter(dto -> dto.getListingType() == type || type == null)
                    .map(ListingCategoryDTO::getName).toArray(String[]::new) : new String[]{};
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryNames);
            categoryAutoCompleteTextView.setAdapter(categoryAdapter);

        });

        listingCategoriesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        listingCategoriesViewModel.fetchActiveListingCategories();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        categoryAutoCompleteTextView.clearFocus();
        super.onDismiss(dialog);
    }
}
