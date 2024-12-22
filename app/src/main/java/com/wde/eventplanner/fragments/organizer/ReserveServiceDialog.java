package com.wde.eventplanner.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.databinding.DialogReserveServiceBinding;
import com.wde.eventplanner.models.event.Event;
import com.wde.eventplanner.models.serviceBudgetItem.BookingSlots;
import com.wde.eventplanner.models.serviceBudgetItem.ReserveService;
import com.wde.eventplanner.models.services.Service;
import com.wde.eventplanner.viewmodels.ServiceBudgetItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReserveServiceDialog extends DialogFragment {
    private ServiceBudgetItemViewModel viewModel;
    private DialogReserveServiceBinding binding;
    private List<BookingSlots> freeSlots;
    private BookingSlots selectedSlots;
    private Service service;

    public ReserveServiceDialog(Service service) {
        this.service = service;
    }

    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogReserveServiceBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ServiceBudgetItemViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener(binding.eventDropdown::onTouchOutsideDropDown);
        }

        binding.eventDropdown.disableAutoComplete();
        binding.startTimeDropdown.disableAutoComplete();
        binding.endTimeDropdown.disableAutoComplete();
        ((CustomDropDown<Event>) binding.eventDropdown).setOnDropdownItemClickListener(this::eventSelected);
        ((CustomDropDown<String>) binding.startTimeDropdown).setOnDropdownItemClickListener(this::startTimeSelected);

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.reserveButton.setOnClickListener(v -> reserveService());

        viewModel.getFreeSlots().observe(getViewLifecycleOwner(), this::onFreeSlotsChanged);
        viewModel.fetchSlotsForService(service);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                viewModel.clearErrorMessage();
            }
        });

        return binding.getRoot();
    }

    @SuppressWarnings("unchecked")
    private void reserveService() {
        Event event = ((CustomDropDown<Event>) binding.eventDropdown).getSelected();
        String startTime = ((CustomDropDown<String>) binding.startTimeDropdown).getSelected();
        String endTime = ((CustomDropDown<String>) binding.endTimeDropdown).getSelected();

        if (event == null || startTime == null || endTime == null)
            Toast.makeText(requireContext(), "Please fill out all the selections!", Toast.LENGTH_SHORT).show();
        else {
            viewModel.reserveService(new ReserveService(event.getId(), service.getStaticServiceId(), startTime, endTime)).observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    Toast.makeText(requireContext(), "Service was successfully reserved!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    private void onFreeSlotsChanged(List<BookingSlots> freeSlots) {
        ArrayList<Event> events = freeSlots.stream().map(BookingSlots::getEvent).collect(Collectors.toCollection(ArrayList::new));
        ((CustomDropDown<Event>) binding.eventDropdown).changeValues(events, Event::getName);
        this.freeSlots = freeSlots;
    }

    @SuppressWarnings("unchecked")
    private void eventSelected(Event event) {
        selectedSlots = freeSlots.stream().filter(s -> s.getEvent().getId().equals(event.getId())).findFirst().orElseThrow();
        ArrayList<String> startTimes = selectedSlots.getTimeTable().keySet().stream().sorted().collect(Collectors.toCollection(ArrayList::new));
        ((CustomDropDown<String>) binding.startTimeDropdown).changeValues(startTimes);
        binding.endTimeDropdown.setEnabled(false);
        binding.endTimeDropdown.clearItems();
    }

    @SuppressWarnings("unchecked")
    private void startTimeSelected(String startTime) {
        ArrayList<String> endTimes = selectedSlots.getTimeTable().get(startTime);
        if (endTimes != null)
            endTimes = endTimes.stream().sorted().collect(Collectors.toCollection(ArrayList::new));
        ((CustomDropDown<String>) binding.endTimeDropdown).changeValues(endTimes);
        if (endTimes != null && endTimes.size() == 1)
            binding.endTimeDropdown.setSelected(0);
        binding.endTimeDropdown.setEnabled(endTimes != null && endTimes.size() != 1);
    }
}