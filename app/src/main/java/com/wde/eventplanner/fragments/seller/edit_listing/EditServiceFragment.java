package com.wde.eventplanner.fragments.seller.edit_listing;

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

import com.wde.eventplanner.adapters.ImageDeletableAdapter;
import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.components.MultiDropDown;
import com.wde.eventplanner.databinding.FragmentEditServiceBinding;
import com.wde.eventplanner.models.event.EventType;
import com.wde.eventplanner.models.services.EditServiceDTO;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;
import com.wde.eventplanner.viewmodels.ServicesViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EditServiceFragment extends Fragment {
    private final ArrayList<Uri> images = new ArrayList<>();
    private ServicesViewModel servicesViewModel;
    private FragmentEditServiceBinding binding;
    private String staticId;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventTypesViewModel eventTypesViewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);
        servicesViewModel = new ViewModelProvider(requireActivity()).get(ServicesViewModel.class);
        binding = FragmentEditServiceBinding.inflate(inflater, container, false);
        staticId = requireArguments().getString("staticId");

        binding.getRoot().setOnTouchListener((v, event) -> {
            binding.inputConfirmationType.onTouchOutsideDropDown(v, event);
            binding.inputAvailability.onTouchOutsideDropDown(v, event);
            binding.inputVisibility.onTouchOutsideDropDown(v, event);
            binding.inputEventTypes.onTouchOutsideDropDown(v, event);
            return false;
        });

        @SuppressWarnings("unchecked")
        CustomDropDown<Boolean> availabilityDropdown = binding.inputAvailability;
        binding.inputAvailability.disableAutoComplete(false);
        availabilityDropdown.setItems(new ArrayList<>(List.of(
                new CustomDropDown.CustomDropDownItem<>("Available", true),
                new CustomDropDown.CustomDropDownItem<>("Unavailable", false))));

        @SuppressWarnings("unchecked")
        CustomDropDown<Boolean> confirmationDropdown = binding.inputConfirmationType;
        binding.inputConfirmationType.disableAutoComplete(false);
        confirmationDropdown.setItems(new ArrayList<>(List.of(
                new CustomDropDown.CustomDropDownItem<>("Manually", true),
                new CustomDropDown.CustomDropDownItem<>("Automatic", false))));

        @SuppressWarnings("unchecked")
        CustomDropDown<Boolean> publicDropdown = binding.inputVisibility;
        binding.inputVisibility.disableAutoComplete(false);
        publicDropdown.setItems(new ArrayList<>(List.of(
                new CustomDropDown.CustomDropDownItem<>("Private", true),
                new CustomDropDown.CustomDropDownItem<>("Public", false))));

        binding.images.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.images.setAdapter(new ImageDeletableAdapter(this, images));
        binding.images.setNestedScrollingEnabled(false);

        binding.editButton.setOnClickListener(v -> editService());

        eventTypesViewModel.getEventTypes().observe(getViewLifecycleOwner(), types -> {
            @SuppressWarnings("unchecked")
            MultiDropDown<EventType> typesDropdown = binding.inputEventTypes;
            typesDropdown.changeValues(types, EventType::getName);
            servicesViewModel.getEditService(staticId).observe(getViewLifecycleOwner(), this::populateServiceData);
        });
        eventTypesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                eventTypesViewModel.clearErrorMessage();
            }
        });
        eventTypesViewModel.fetchEventTypes();

        servicesViewModel.getService(staticId).observe(getViewLifecycleOwner(), s -> {
            FileManager.downloadImagesToLocal(requireContext(), s.getImages(), images, (ImageDeletableAdapter) binding.images.getAdapter());
        });

        return binding.getRoot();
    }

    private void populateServiceData(EditServiceDTO service) {
        binding.inputName.setText(service.getName());
        binding.inputAvailability.setSelected(service.getIsAvailable() ? 0 : 1);
        binding.inputPrice.setText(String.format(Locale.US, "%d", service.getPrice().intValue()));
        binding.inputDiscount.setText(String.format(Locale.US, "%d", Double.valueOf(100 * service.getSalePercentage()).intValue()));
        binding.inputReservationPeriod.setText(String.format(Locale.US, "%d", service.getReservationDeadline()));
        binding.inputCancellationPeriod.setText(String.format(Locale.US, "%d", service.getCancellationDeadline()));
        binding.inputConfirmationType.setSelected(service.getIsConfirmationManual() ? 0 : 1);
        binding.inputVisibility.setSelected(service.getIsPrivate() ? 0 : 1);
        binding.inputMinDuration.setText(String.format(Locale.US, "%d", service.getMinimumDuration()));
        binding.inputMaxDuration.setText(String.format(Locale.US, "%d", service.getMaximumDuration()));
        binding.inputDescription.setText(service.getDescription());

        @SuppressWarnings("unchecked")
        MultiDropDown<EventType> eventTypesDropdown = binding.inputEventTypes;
        eventTypesDropdown.setSelected(e -> service.getAvailableEventTypeIds().stream().anyMatch(id -> e.getId().equals(id.toString())));
    }

    public void editService() {
        if (binding.inputName.getText() == null || binding.inputDescription.getText() == null || binding.inputPrice.getText() == null
                || binding.inputDiscount.getText() == null || binding.inputReservationPeriod.getText() == null || binding.inputCancellationPeriod.getText() == null
                || binding.inputMinDuration.getText() == null || binding.inputMaxDuration.getText() == null) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        String name = binding.inputName.getText().toString().trim();
        Boolean isAvailable = (Boolean) binding.inputAvailability.getSelected();
        Boolean isConfirmationManual = (Boolean) binding.inputConfirmationType.getSelected();
        Boolean isPrivate = (Boolean) binding.inputVisibility.getSelected();
        String description = binding.inputDescription.getText().toString().trim();

        @SuppressWarnings("unchecked")
        ArrayList<EventType> availableEventTypes = binding.inputEventTypes.getSelected();

        // Input Validation
        if (name.isBlank() || isAvailable == null || isConfirmationManual == null || isPrivate == null || description.isBlank()
                || availableEventTypes == null || availableEventTypes.isEmpty()) {
            SingleToast.show(requireContext(), "Please fill in all the required fields");
            return;
        }

        ArrayList<String> availableEventTypeIds = availableEventTypes.stream().map(EventType::getId).collect(Collectors.toCollection(ArrayList::new));

        double price, salePercentage;
        int reservationDeadline, cancellationDeadline, minimumDuration, maximumDuration;
        try {
            price = Double.parseDouble(binding.inputPrice.getText().toString().trim());
            salePercentage = Double.parseDouble(binding.inputDiscount.getText().toString().trim()) / 100;
            reservationDeadline = Integer.parseInt(binding.inputReservationPeriod.getText().toString().trim());
            cancellationDeadline = Integer.parseInt(binding.inputCancellationPeriod.getText().toString().trim());
            minimumDuration = Integer.parseInt(binding.inputMinDuration.getText().toString().trim());
            maximumDuration = Integer.parseInt(binding.inputMaxDuration.getText().toString().trim());
        } catch (Exception e) {
            SingleToast.show(requireContext(), "Please fill in all the required fields");
            return;
        }

        if (salePercentage > 1) {
            SingleToast.show(requireContext(), "Discount must be smaller than 100%");
            return;
        }

        ArrayList<File> imageFiles = new ArrayList<>();
        try {
            for (Uri image : images)
                imageFiles.add(FileManager.getFileFromUri(requireContext(), image));
        } catch (Exception e) {
            SingleToast.show(requireContext(), "Failed to load the images");
            imageFiles = null;
        }

        servicesViewModel.updateService(imageFiles, staticId, name, isAvailable, price, salePercentage, reservationDeadline, cancellationDeadline, isConfirmationManual,
                isPrivate, minimumDuration, maximumDuration, description, availableEventTypeIds).observe(getViewLifecycleOwner(), service -> {
            getParentFragmentManager().popBackStack();
        });
    }
}
