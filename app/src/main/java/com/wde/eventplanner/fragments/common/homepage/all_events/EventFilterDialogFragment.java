package com.wde.eventplanner.fragments.common.homepage.all_events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.models.EventType;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventFilterDialogFragment extends DialogFragment {
    public interface EventsFilterListener {
        void onFilterPressed(String category, String city, Date after, Date before, String minRating, String maxRating);
    }

    private final EventsFilterListener parent;
    private DialogEventFilterBinding binding;
    private final Date before = new Date(0);
    private final Date after = new Date(0);
    private boolean calendarIsOpen = false;

    public EventFilterDialogFragment(EventsFilterListener fragment) {
        this.parent = fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEventFilterBinding.inflate(inflater, container, false);
        EventTypesViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener((v, event) -> {
                binding.typeDropdown.onTouchOutsideDropDown(v, event);
                binding.cityDropdown.onTouchOutsideDropDown(v, event);
                v.performClick();
                return false;
            });
        }

        @SuppressWarnings("unchecked")
        CustomDropDown<String> cityDropdown = binding.cityDropdown;
        ArrayList<String> cities = new ArrayList<>(Arrays.asList(binding.getRoot().getContext().getResources().getStringArray(R.array.cities)));
        cityDropdown.changeValues(cities, String::toString);

        binding.afterDate.setOnClickListener(v -> {
            if (!calendarIsOpen) openDatePicker(binding.afterDate, after);
        });
        binding.beforeDate.setOnClickListener(v -> {
            if (!calendarIsOpen) openDatePicker(binding.beforeDate, before);
        });

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.filterButton.setOnClickListener(this::onFilterButtonClicked);

        viewModel.getEventTypes().observe(getViewLifecycleOwner(), this::OnTypesChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                viewModel.clearErrorMessage();
            }
        });
        viewModel.fetchEventTypes();

        return binding.getRoot();
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
        EventType eventType = (EventType) binding.typeDropdown.getSelected();
        String type = eventType != null ? eventType.getId() : null;
        String city = (String) binding.cityDropdown.getSelected();

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
        @SuppressWarnings("unchecked")
        CustomDropDown<EventType> typeDropdown = binding.typeDropdown;
        typeDropdown.changeValues(types, EventType::getName);
    }
}
