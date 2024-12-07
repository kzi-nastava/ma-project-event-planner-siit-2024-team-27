package com.wde.eventplanner.fragments.common.homepage.all_events;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.DialogEventFilterBinding;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class EventFilterDialogFragment extends DialogFragment {
    private ArrayList<ListingCategoryDTO> categories;
    private DialogEventFilterBinding binding;
    private final Date before = new Date(0);
    private final Date after = new Date(0);
    private final AllEventsFragment parent;
    private boolean calendarIsOpen = false;
    private String[] cities;

    public EventFilterDialogFragment(AllEventsFragment fragment) {
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
        binding = DialogEventFilterBinding.inflate(inflater, container, false);

        cities = binding.getRoot().getContext().getResources().getStringArray(R.array.cities);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, cities);
        binding.cityDropdown.setAdapter(cityAdapter);
        binding.cityDropdown.setOnClickListener(v -> binding.cityDropdown.showDropDown());
        binding.cityDropdown.setOnFocusChangeListener((v, hasFocus) -> {
            String input = binding.cityDropdown.getText().toString().trim();
            if (hasFocus) binding.cityDropdown.showDropDown();
            else {
                Optional<String> found = Arrays.stream(cities).filter(city -> city.equalsIgnoreCase(input)).findFirst();
                binding.cityDropdown.setText(found.orElse(""));
            }
        });

        binding.getRoot().setOnTouchListener((v, event) -> {
            Rect rect = new Rect();
            binding.cityDropdown.getGlobalVisibleRect(rect);
            if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                binding.cityDropdown.clearFocus();
                InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.cityDropdown.getWindowToken(), 0);
            }
            binding.categoryDropdown.getGlobalVisibleRect(rect);
            if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                binding.categoryDropdown.clearFocus();
                InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.categoryDropdown.getWindowToken(), 0);
            }
            return false;
        });


        binding.afterDate.setOnClickListener(v -> {
            if (!calendarIsOpen) {
                calendarIsOpen = true;
                openDatePicker(binding.afterDate, after);
            }
        });
        binding.beforeDate.setOnClickListener(v -> {
            if (!calendarIsOpen) {
                calendarIsOpen = true;
                openDatePicker(binding.beforeDate, before);
            }
        });

        binding.closeButton.setOnClickListener(v -> dismiss());


        binding.filterButton.setOnClickListener(v -> {
            String categoryInput = binding.categoryDropdown.getText().toString().trim();
            String category = categories.stream().filter(dto -> dto.getName().equalsIgnoreCase(categoryInput)).findFirst().map(ListingCategoryDTO::getId).orElse(null);

            String cityInput = binding.cityDropdown.getText().toString().trim();
            String city = Arrays.stream(cities).filter(ct -> ct.equalsIgnoreCase(cityInput)).findFirst().orElse(null);

            Date afterReturn = after.after(new Date(1)) ? after : null;
            Date beforeReturn = before.after(new Date(1)) ? before : null;

            String minRating, maxRating;
            if (binding.minRating.getText() != null && !binding.minRating.getText().toString().isBlank())
                minRating = binding.minRating.getText().toString();
            else minRating = null;
            if (binding.maxRating.getText() != null && !binding.maxRating.getText().toString().isBlank())
                maxRating = binding.maxRating.getText().toString();
            else maxRating = null;

            parent.onFilterPressed(category, city, afterReturn, beforeReturn, minRating, maxRating);
            dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // todo: Zameniti posle sa event categories
        ListingCategoriesViewModel listingCategoriesViewModel = new ViewModelProvider(this).get(ListingCategoriesViewModel.class);

        listingCategoriesViewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), categoryDTOs -> {
            this.categories = categoryDTOs;
            String[] categories = categoryDTOs.stream().map(ListingCategoryDTO::getName).toArray(String[]::new);
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
            binding.categoryDropdown.setAdapter(categoryAdapter);
            binding.categoryDropdown.setOnClickListener(v -> binding.categoryDropdown.showDropDown());
            binding.categoryDropdown.setOnFocusChangeListener((v, hasFocus) -> {
                String input = binding.categoryDropdown.getText().toString().trim();
                if (hasFocus) binding.categoryDropdown.showDropDown();
                else {
                    Optional<String> found = Arrays.stream(categories)
                            .filter(category -> category.equalsIgnoreCase(input)).findFirst();
                    binding.categoryDropdown.setText(found.orElse(""));
                }
            });
        });

        listingCategoriesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        listingCategoriesViewModel.fetchActiveListingCategories();
    }

    private void openDatePicker(TextInputEditText editText, Date date) {
        CalendarConstraints constraint = new CalendarConstraints.Builder().setFirstDayOfWeek(Calendar.MONDAY).build();

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraint)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).setTheme(R.style.DatePickerWithoutHeader).build();

        datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            date.setTime(selection);
            editText.setText(new SimpleDateFormat("d.M.yyyy.", Locale.ENGLISH).format(date));
            calendarIsOpen = false;
        });

        datePicker.addOnNegativeButtonClickListener(selection -> {
            date.setTime(0);
            editText.setText("");
            calendarIsOpen = false;
        });

        datePicker.addOnNegativeButtonClickListener(v -> calendarIsOpen = false);
    }
}
