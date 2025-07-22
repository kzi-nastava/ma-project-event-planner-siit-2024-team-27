package com.wde.eventplanner.fragments.seller.create_listing;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.ImageDeletableAdapter;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.components.MultiDropDown;
import com.wde.eventplanner.databinding.FragmentCreateServiceBinding;
import com.wde.eventplanner.models.event.EventType;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;
import com.wde.eventplanner.viewmodels.ServicesViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateServiceFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private final ArrayList<Uri> images = new ArrayList<>();
    private FragmentCreateServiceBinding binding;
    private ServicesViewModel servicesViewModel;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListingCategoriesViewModel listingCategoriesViewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);
        EventTypesViewModel eventTypesViewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);
        servicesViewModel = new ViewModelProvider(requireActivity()).get(ServicesViewModel.class);
        binding = FragmentCreateServiceBinding.inflate(inflater, container, false);

        binding.getRoot().setOnTouchListener((v, event) -> {
            binding.inputConfirmationType.onTouchOutsideDropDown(v, event);
            binding.inputServiceCategory.onTouchOutsideDropDown(v, event);
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
                new CustomDropDown.CustomDropDownItem<>("Public", false),
                new CustomDropDown.CustomDropDownItem<>("Private", true))));

        binding.inputSuggestionCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().isEmpty()) {
                    binding.serviceCategoryLayout.setEnabled(true);
                    binding.categoryDescriptionLayout.setEnabled(false);
                } else {
                    binding.serviceCategoryLayout.setEnabled(false);
                    binding.categoryDescriptionLayout.setEnabled(true);
                }
            }
        });

        binding.images.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.images.setAdapter(new ImageDeletableAdapter(this, images));
        binding.images.setNestedScrollingEnabled(false);

        binding.createButton.setOnClickListener(v -> createService());

        listingCategoriesViewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), categories -> {
            @SuppressWarnings("unchecked")
            CustomDropDown<ListingCategory> categoryDropdown = binding.inputServiceCategory;
            categories = categories.stream().filter(c -> c.getListingType() == ListingType.SERVICE).collect(Collectors.toCollection(ArrayList::new));
            categoryDropdown.changeValues(categories, ListingCategory::getName);
        });
        listingCategoriesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                listingCategoriesViewModel.clearErrorMessage();
            }
        });
        listingCategoriesViewModel.fetchActiveListingCategories();

        eventTypesViewModel.getEventTypes().observe(getViewLifecycleOwner(), types -> {
            @SuppressWarnings("unchecked")
            MultiDropDown<EventType> typesDropdown = binding.inputEventTypes;
            typesDropdown.changeValues(types, EventType::getName);
        });
        eventTypesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                eventTypesViewModel.clearErrorMessage();
            }
        });
        eventTypesViewModel.fetchEventTypes();

        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "Service";
    }

    public void createService() {
        if (binding.inputName.getText() == null || binding.inputDescription.getText() == null || binding.inputSuggestionCategory.getText() == null
                || binding.inputCategoryDescription.getText() == null || binding.inputPrice.getText() == null || binding.inputDiscount.getText() == null
                || binding.inputReservationPeriod.getText() == null || binding.inputCancellationPeriod.getText() == null || binding.inputMinDuration.getText() == null
                || binding.inputMaxDuration.getText() == null) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        String name = binding.inputName.getText().toString().trim();
        Boolean isAvailable = (Boolean) binding.inputAvailability.getSelected();
        Boolean isConfirmationManual = (Boolean) binding.inputConfirmationType.getSelected();
        Boolean isPrivate = (Boolean) binding.inputVisibility.getSelected();
        String description = binding.inputDescription.getText().toString().trim();
        String suggestedCategory = binding.inputSuggestionCategory.getText().toString().trim();
        String suggestedCategoryDescription = binding.inputCategoryDescription.getText().toString().trim();

        @SuppressWarnings("unchecked")
        CustomDropDown<ListingCategory> categoryDropdown = binding.inputServiceCategory;
        String serviceCategoryId = categoryDropdown.getSelected() == null ? null : categoryDropdown.getSelected().getId();
        @SuppressWarnings("unchecked")
        ArrayList<EventType> availableEventTypes = binding.inputEventTypes.getSelected();

        // Input Validation
        if (name.isBlank() || isAvailable == null || isConfirmationManual == null || isPrivate == null || description.isBlank()
                || ((serviceCategoryId == null || serviceCategoryId.isBlank()) && (suggestedCategory.isBlank() || suggestedCategoryDescription.isBlank()))
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

        if (suggestedCategory.isBlank())
            suggestedCategoryDescription = "";
        else
            serviceCategoryId = "";

        servicesViewModel.createService(imageFiles, TokenManager.getUserId(binding.getRoot().getContext()), name, isAvailable, price, salePercentage,
                serviceCategoryId, reservationDeadline, cancellationDeadline, isConfirmationManual, isPrivate, minimumDuration, maximumDuration, description,
                suggestedCategory, suggestedCategoryDescription, availableEventTypeIds).observe(getViewLifecycleOwner(), service -> {
            getParentFragmentManager().popBackStack();
        });
    }
}
