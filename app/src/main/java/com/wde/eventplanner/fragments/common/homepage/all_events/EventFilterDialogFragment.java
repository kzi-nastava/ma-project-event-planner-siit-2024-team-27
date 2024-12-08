package com.wde.eventplanner.fragments.common.homepage.all_events;

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

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.DialogEventFilterBinding;
import com.wde.eventplanner.models.EventType;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventFilterDialogFragment extends DialogFragment {
    private DialogEventFilterBinding binding;
    private final Date before = new Date(0);
    private final Date after = new Date(0);
    private final AllEventsFragment parent;
    private boolean calendarIsOpen = false;
    private ArrayList<String> typeNames;
    private ArrayList<EventType> types;
    private ArrayList<String> cities;

    public EventFilterDialogFragment(AllEventsFragment fragment) {
        this.parent = fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEventFilterBinding.inflate(inflater, container, false);
        EventTypesViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener(this::onTouchOutsideDialog);
        }

        cities = new ArrayList<>(Arrays.asList(binding.getRoot().getContext().getResources().getStringArray(R.array.cities)));
        binding.cityDropdown.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, cities));
        binding.cityDropdown.setOnFocusChangeListener((v, hasFocus) -> onDropdownFocusChanged(hasFocus, binding.cityDropdown, cities));
        binding.cityDropdown.setOnClickListener(v -> binding.cityDropdown.showDropDown());

        binding.typeDropdown.setOnFocusChangeListener((v, hasFocus) -> onDropdownFocusChanged(hasFocus, binding.typeDropdown, typeNames));
        binding.typeDropdown.setOnClickListener(v -> binding.typeDropdown.showDropDown());

        binding.afterDate.setOnClickListener(v -> {
            if (!calendarIsOpen) openDatePicker(binding.afterDate, after);
        });
        binding.beforeDate.setOnClickListener(v -> {
            if (!calendarIsOpen) openDatePicker(binding.beforeDate, before);
        });

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.filterButton.setOnClickListener(this::onFilterButtonClicked);

        viewModel.getActiveEventTypes().observe(getViewLifecycleOwner(), this::OnTypesChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });
        viewModel.fetchActiveEventTypes();

        return binding.getRoot();
    }

    private boolean onTouchOutsideDialog(View v, MotionEvent event) {
        Rect rect = new Rect();
        binding.cityDropdown.getGlobalVisibleRect(rect);
        if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            binding.cityDropdown.clearFocus();
            InputMethodManager imm = (InputMethodManager) binding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.cityDropdown.getWindowToken(), 0);
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
        if (hasFocus) {
            dropdown.setText("");
            dropdown.showDropDown();
        } else {
            Optional<String> found = values.stream().filter(value -> value.equalsIgnoreCase(input)).findFirst();
            dropdown.setText(found.orElse(""));
        }
    }

    private void openDatePicker(TextInputEditText editText, Date date) {
        calendarIsOpen = true;
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
        datePicker.addOnDismissListener(v -> calendarIsOpen = false);
    }

    private void onFilterButtonClicked(View v) {
        String typeInput = binding.typeDropdown.getText().toString().trim();
        String type = types.stream().filter(dto -> dto.getName().equalsIgnoreCase(typeInput)).findFirst().map(EventType::getId).orElse(null);

        String cityInput = binding.cityDropdown.getText().toString().trim();
        String city = cities.stream().filter(ct -> ct.equalsIgnoreCase(cityInput)).findFirst().orElse(null);

        Date afterReturn = after.after(new Date(1)) ? after : null;
        Date beforeReturn = before.after(new Date(1)) ? before : null;

        String minRating, maxRating;
        if (binding.minRating.getText() != null && !binding.minRating.getText().toString().isBlank())
            minRating = binding.minRating.getText().toString();
        else minRating = null;
        if (binding.maxRating.getText() != null && !binding.maxRating.getText().toString().isBlank())
            maxRating = binding.maxRating.getText().toString();
        else maxRating = null;

        parent.onFilterPressed(type, city, afterReturn, beforeReturn, minRating, maxRating);
        dismiss();
    }

    private void OnTypesChanged(ArrayList<EventType> types) {
        this.types = types;
        typeNames = (ArrayList<String>) types.stream().map(EventType::getName).collect(Collectors.toList());
        binding.typeDropdown.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, typeNames));
    }
}
