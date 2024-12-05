package com.wde.eventplanner.fragments.homepage.all_events;

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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.R;
import com.wde.eventplanner.fragments.AdminListingCategories.AdminListingCategoriesViewModel;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class EventFilterDialogFragment extends DialogFragment {
    private MaterialAutoCompleteTextView categoryAutoCompleteTextView;
    private ArrayList<ListingCategoryDTO> categories;
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
        View view = inflater.inflate(R.layout.fragment_event_filter_dialog, null);

        categoryAutoCompleteTextView = view.findViewById(R.id.categoryDropdown);

        cities = view.getContext().getResources().getStringArray(R.array.cities);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, cities);
        MaterialAutoCompleteTextView cityAutoCompleteTextView = view.findViewById(R.id.cityDropdown);
        cityAutoCompleteTextView.setAdapter(cityAdapter);
        cityAutoCompleteTextView.setOnClickListener(v -> cityAutoCompleteTextView.showDropDown());
        cityAutoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
            String input = cityAutoCompleteTextView.getText().toString().trim();
            if (hasFocus) cityAutoCompleteTextView.showDropDown();
            else {
                Optional<String> found = Arrays.stream(cities).filter(city -> city.equalsIgnoreCase(input)).findFirst();
                cityAutoCompleteTextView.setText(found.orElse(""));
            }
        });

        view.setOnTouchListener((v, event) -> {
            Rect rect = new Rect();
            cityAutoCompleteTextView.getGlobalVisibleRect(rect);
            if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                cityAutoCompleteTextView.clearFocus();
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cityAutoCompleteTextView.getWindowToken(), 0);
            }
            categoryAutoCompleteTextView.getGlobalVisibleRect(rect);
            if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                categoryAutoCompleteTextView.clearFocus();
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(categoryAutoCompleteTextView.getWindowToken(), 0);
            }
            return false;
        });

        TextInputEditText afterDate = view.findViewById(R.id.afterDate);
        TextInputEditText beforeDate = view.findViewById(R.id.beforeDate);

        afterDate.setOnClickListener(v -> {
            if (!calendarIsOpen) {
                calendarIsOpen = true;
                openDatePicker(afterDate, after);
            }
        });
        beforeDate.setOnClickListener(v -> {
            if (!calendarIsOpen) {
                calendarIsOpen = true;
                openDatePicker(beforeDate, before);
            }
        });

        Button closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dismiss());

        TextInputEditText minRatingInput = view.findViewById(R.id.minRating);
        TextInputEditText maxRatingInput = view.findViewById(R.id.maxRating);

        Button filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> {
            String categoryInput = categoryAutoCompleteTextView.getText().toString().trim();
            String category = categories.stream().filter(dto -> dto.getName().equalsIgnoreCase(categoryInput)).findFirst().map(ListingCategoryDTO::getId).orElse(null);

            String cityInput = cityAutoCompleteTextView.getText().toString().trim();
            String city = Arrays.stream(cities).filter(ct -> ct.equalsIgnoreCase(cityInput)).findFirst().orElse(null);

            Date afterReturn = after.after(new Date(1)) ? after : null;
            Date beforeReturn = before.after(new Date(1)) ? before : null;

            String minRating, maxRating;
            if (minRatingInput.getText() != null && !minRatingInput.getText().toString().isBlank())
                minRating = minRatingInput.getText().toString();
            else minRating = null;
            if (maxRatingInput.getText() != null && !maxRatingInput.getText().toString().isBlank())
                maxRating = maxRatingInput.getText().toString();
            else maxRating = null;

            parent.onFilterPressed(category, city, afterReturn, beforeReturn, minRating, maxRating);
            dismiss();
        });

        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Zameniti posle sa event categories
        AdminListingCategoriesViewModel listingCategoriesViewModel = new ViewModelProvider(this).get(AdminListingCategoriesViewModel.class);

        listingCategoriesViewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), categoryDTOs -> {
            this.categories = categoryDTOs;
            String[] categories = categoryDTOs.stream().map(ListingCategoryDTO::getName).toArray(String[]::new);
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
            categoryAutoCompleteTextView.setAdapter(categoryAdapter);
            categoryAutoCompleteTextView.setOnClickListener(v -> categoryAutoCompleteTextView.showDropDown());
            categoryAutoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
                String input = categoryAutoCompleteTextView.getText().toString().trim();
                if (hasFocus) categoryAutoCompleteTextView.showDropDown();
                else {
                    Optional<String> found = Arrays.stream(categories)
                            .filter(category -> category.equalsIgnoreCase(input)).findFirst();
                    categoryAutoCompleteTextView.setText(found.orElse(""));
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
