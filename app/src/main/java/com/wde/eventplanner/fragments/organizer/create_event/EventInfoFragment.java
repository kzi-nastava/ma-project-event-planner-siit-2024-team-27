package com.wde.eventplanner.fragments.organizer.create_event;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.ImageDeletableAdapter;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.models.event.CreateEventDTO;
import com.wde.eventplanner.models.event.EventType;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.databinding.FragmentEventInfoBinding;
import com.wde.eventplanner.models.event.GuestInfo;
import com.wde.eventplanner.models.event.GuestList;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.CreateEventViewModel;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;
import com.wde.eventplanner.viewmodels.InvitationsViewModel;
import com.wde.eventplanner.viewmodels.EventsViewModel;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EventInfoFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private final ArrayList<Uri> images = new ArrayList<>();
    private FragmentEventInfoBinding binding;
    private boolean calendarIsOpen = false;
    private String time = "";
    private LocalDate date;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventInfoBinding.inflate(inflater, container, false);
        EventTypesViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);

        binding.publicDropdown.disableAutoComplete(false);
        binding.createButton.setOnClickListener(this::onCreateEventClicked);
        binding.getRoot().setOnTouchListener((v, event) -> {
            binding.categoryDropdown.onTouchOutsideDropDown(v, event);
            binding.publicDropdown.onTouchOutsideDropDown(v, event);
            binding.cityDropdown.onTouchOutsideDropDown(v, event);
            return false;
        });

        @SuppressWarnings("unchecked")
        CustomDropDown<String> cityDropdown = binding.cityDropdown;
        cityDropdown.changeValues(new ArrayList<>(Arrays.asList(binding.getRoot().getContext().getResources().getStringArray(R.array.cities))));

        @SuppressWarnings("unchecked")
        CustomDropDown<Boolean> publicDropdown = binding.publicDropdown;
        publicDropdown.setItems(new ArrayList<>(List.of(
                new CustomDropDown.CustomDropDownItem<>("Public", true),
                new CustomDropDown.CustomDropDownItem<>("Private", false))));
        publicDropdown.setSelected(0);

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select time")
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            time = LocalTime.of(timePicker.getHour(), timePicker.getMinute()).format(DateTimeFormatter.ofPattern("HH:mm"));
            binding.timeInput.setText(time);
        });

        timePicker.addOnNegativeButtonClickListener(v -> {
            binding.timeInput.setText("");
            time = "";
        });

        binding.timeInput.setOnClickListener(v -> timePicker.show(getParentFragmentManager(), "time_picker"));

        binding.datePicker.setOnClickListener(v -> {
            if (!calendarIsOpen) openDatePicker();
        });

        binding.images.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        ImageDeletableAdapter adapter = new ImageDeletableAdapter(this, images);
        binding.images.setNestedScrollingEnabled(false);
        binding.images.setAdapter(adapter);

        viewModel.getEventTypes().observe(getViewLifecycleOwner(), this::OnCategoriesChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                viewModel.clearErrorMessage();
            }
        });
        viewModel.fetchEventTypes();

        return binding.getRoot();
    }

    private void OnCategoriesChanged(ArrayList<EventType> types) {
        @SuppressWarnings("unchecked")
        CustomDropDown<EventType> categoryDropdown = binding.categoryDropdown;
        categoryDropdown.changeValues(types, EventType::getName);
    }

    private void openDatePicker() {
        calendarIsOpen = true;
        CalendarConstraints constraint = new CalendarConstraints.Builder().setFirstDayOfWeek(Calendar.MONDAY).build();

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraint)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).setTheme(R.style.DatePickerWithoutHeader).build();

        datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            date = LocalDate.ofEpochDay(TimeUnit.MILLISECONDS.toDays(selection));
            binding.datePicker.setText(date.format(DateTimeFormatter.ofPattern("d.M.yyyy.")));
            calendarIsOpen = false;
        });

        datePicker.addOnNegativeButtonClickListener(selection -> {
            binding.datePicker.setText("");
            calendarIsOpen = false;
            date = null;
        });

        datePicker.addOnNegativeButtonClickListener(v -> calendarIsOpen = false);
        datePicker.addOnDismissListener(v -> calendarIsOpen = false);
    }

    private void onCreateEventClicked(View v) {
        if (binding.nameInput.getText() == null || binding.addressInput.getText() == null || binding.guestCountInput.getText() == null || binding.descriptionInput.getText() == null) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        String name = binding.nameInput.getText().toString().trim();
        String address = binding.addressInput.getText().toString().trim();
        String guestCount = binding.guestCountInput.getText().toString().trim();
        String description = binding.descriptionInput.getText().toString().trim();
        String city = (String) binding.cityDropdown.getSelected();
        @SuppressWarnings("unchecked")
        CustomDropDown<EventType> categoryDropdown = binding.categoryDropdown;
        EventType category = categoryDropdown.getSelected();
        @SuppressWarnings("unchecked")
        CustomDropDown<Boolean> isPublicDropdown = binding.publicDropdown;
        Boolean isPublic = isPublicDropdown.getSelected();

        // Input Validation
        if (name.isBlank() || address.isBlank() || guestCount.isBlank() || city == null || city.isBlank() || category == null || category.getId().isBlank() || date == null || time.isBlank()) {
            SingleToast.show(requireContext(), "Please fill in all the required fields");
            return;
        }

        int count;
        try {
            count = Integer.parseInt(binding.guestCountInput.getText().toString().trim());
        } catch (Exception e) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        CreateEventViewModel createEventViewModel = new ViewModelProvider(requireActivity()).get(CreateEventViewModel.class);
        InvitationsViewModel invitationsViewModel = new ViewModelProvider(requireActivity()).get(InvitationsViewModel.class);
        EventsViewModel eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);

        createEventViewModel.agendaItems.removeIf(item -> !item.isFilled());
        eventsViewModel.createAgenda(createEventViewModel.agendaItems).observe(getViewLifecycleOwner(), ids -> {
            String dateFormatted = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000'Z'"));
            eventsViewModel.createEvent(new CreateEventDTO(name, description, city, address, isPublic, dateFormatted, time, count, category.getId(), ids, TokenManager.getProfileId(requireContext()), 1, 1)).observe(getViewLifecycleOwner(), event -> {
                ArrayList<String> emails = createEventViewModel.guestList.stream().map(GuestInfo::getEmail).collect(Collectors.toCollection(ArrayList::new));
                GuestList guestList = new GuestList(emails, event.getId().toString(), "OrgName", "OrgSurname");  // todo replace fixed params
                if (!emails.isEmpty()) invitationsViewModel.sendInvitations(guestList).observe(getViewLifecycleOwner(), error -> {
                    if (error != null) SingleToast.show(requireContext(), error);
                });

                try {
                    ArrayList<File> imageFiles = new ArrayList<>();
                    for (Uri image : images)
                        imageFiles.add(FileManager.getFileFromUri(requireContext(), image));

                    eventsViewModel.putImages(imageFiles, event.getId()).observe(getViewLifecycleOwner(), response2 -> {
                        if (!response2.isSuccessful())
                            SingleToast.show(requireContext(), "Failed to add images to the event!");

                        getParentFragmentManager().popBackStack();
                    });
                } catch (Exception e) {
                    SingleToast.show(requireContext(), "Failed to load the images");
                }
            });
        });
    }

    @Override
    public String getTitle() {
        return "Info";
    }
}