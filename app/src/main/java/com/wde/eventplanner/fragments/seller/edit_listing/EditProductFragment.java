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
import com.wde.eventplanner.databinding.FragmentEditProductBinding;
import com.wde.eventplanner.models.event.EventType;
import com.wde.eventplanner.models.products.EditProductDTO;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;
import com.wde.eventplanner.viewmodels.ProductsViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EditProductFragment extends Fragment {
    private final ArrayList<Uri> images = new ArrayList<>();
    private ProductsViewModel productsViewModel;
    private FragmentEditProductBinding binding;
    private String staticId;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventTypesViewModel eventTypesViewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);
        productsViewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);
        binding = FragmentEditProductBinding.inflate(inflater, container, false);
        staticId = requireArguments().getString("staticId");

        binding.getRoot().setOnTouchListener((v, event) -> {
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
        CustomDropDown<Boolean> publicDropdown = binding.inputVisibility;
        binding.inputVisibility.disableAutoComplete(false);
        publicDropdown.setItems(new ArrayList<>(List.of(
                new CustomDropDown.CustomDropDownItem<>("Private", true),
                new CustomDropDown.CustomDropDownItem<>("Public", false))));

        binding.images.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.images.setAdapter(new ImageDeletableAdapter(this, images));
        binding.images.setNestedScrollingEnabled(false);

        binding.editButton.setOnClickListener(v -> editProduct());

        eventTypesViewModel.getEventTypes().observe(getViewLifecycleOwner(), types -> {
            @SuppressWarnings("unchecked")
            MultiDropDown<EventType> typesDropdown = binding.inputEventTypes;
            typesDropdown.changeValues(types, EventType::getName);
            productsViewModel.getEditProduct(staticId).observe(getViewLifecycleOwner(), this::populateProductData);
        });
        eventTypesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                eventTypesViewModel.clearErrorMessage();
            }
        });
        eventTypesViewModel.fetchEventTypes();

        productsViewModel.getProduct(staticId).observe(getViewLifecycleOwner(), p -> {
            FileManager.downloadImagesToLocal(requireContext(), p.getImages(), images, (ImageDeletableAdapter) binding.images.getAdapter());
        });

        return binding.getRoot();
    }

    private void populateProductData(EditProductDTO product) {
        binding.inputName.setText(product.getName());
        binding.inputAvailability.setSelected(product.getIsAvailable() ? 0 : 1);
        binding.inputPrice.setText(String.format(Locale.US, "%d", product.getPrice().intValue()));
        binding.inputDiscount.setText(String.format(Locale.US, "%d", Double.valueOf(100 * product.getSalePercentage()).intValue()));
        binding.inputVisibility.setSelected(product.getIsPrivate() ? 0 : 1);
        binding.inputDescription.setText(product.getDescription());

        @SuppressWarnings("unchecked")
        MultiDropDown<EventType> eventTypesDropdown = binding.inputEventTypes;
        eventTypesDropdown.setSelected(e ->
                product.getAvailableEventTypeIds().stream().anyMatch(id -> e.getId().equals(id.toString()))
        );
    }

    public void editProduct() {
        if (binding.inputName.getText() == null || binding.inputDescription.getText() == null || binding.inputPrice.getText() == null || binding.inputDiscount.getText() == null) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        String name = binding.inputName.getText().toString().trim();
        Boolean isAvailable = (Boolean) binding.inputAvailability.getSelected();
        Boolean isPrivate = (Boolean) binding.inputVisibility.getSelected();
        String description = binding.inputDescription.getText().toString().trim();

        @SuppressWarnings("unchecked")
        ArrayList<EventType> availableEventTypes = binding.inputEventTypes.getSelected();

        // Input Validation
        if (name.isBlank() || isAvailable == null || isPrivate == null || description.isBlank()
                || availableEventTypes == null || availableEventTypes.isEmpty()) {
            SingleToast.show(requireContext(), "Please fill in all the required fields");
            return;
        }

        ArrayList<String> availableEventTypeIds = availableEventTypes.stream().map(EventType::getId).collect(Collectors.toCollection(ArrayList::new));

        double price, salePercentage;
        try {
            price = Double.parseDouble(binding.inputPrice.getText().toString().trim());
            salePercentage = Double.parseDouble(binding.inputDiscount.getText().toString().trim()) / 100;
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

        productsViewModel.updateProduct(imageFiles, staticId, name, isAvailable, price, salePercentage, isPrivate, description, availableEventTypeIds)
                .observe(getViewLifecycleOwner(), product -> getParentFragmentManager().popBackStack());
    }
}
