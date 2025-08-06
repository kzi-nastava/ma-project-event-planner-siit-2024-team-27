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
import com.wde.eventplanner.databinding.FragmentCreateProductBinding;
import com.wde.eventplanner.models.event.EventType;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;
import com.wde.eventplanner.viewmodels.ProductsViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateProductFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private final ArrayList<Uri> images = new ArrayList<>();
    private FragmentCreateProductBinding binding;
    private ProductsViewModel productsViewModel;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListingCategoriesViewModel listingCategoriesViewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);
        EventTypesViewModel eventTypesViewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);
        productsViewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);
        binding = FragmentCreateProductBinding.inflate(inflater, container, false);

        binding.getRoot().setOnTouchListener((v, event) -> {
            binding.inputProductCategory.onTouchOutsideDropDown(v, event);
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
                    binding.productCategoryLayout.setEnabled(true);
                    binding.categoryDescriptionLayout.setEnabled(false);
                } else {
                    binding.productCategoryLayout.setEnabled(false);
                    binding.categoryDescriptionLayout.setEnabled(true);
                }
            }
        });

        binding.images.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.images.setAdapter(new ImageDeletableAdapter(this, images));
        binding.images.setNestedScrollingEnabled(false);

        binding.createButton.setOnClickListener(v -> createProduct());

        listingCategoriesViewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), categories -> {
            @SuppressWarnings("unchecked")
            CustomDropDown<ListingCategory> categoryDropdown = binding.inputProductCategory;
            categories = categories.stream().filter(c -> c.getListingType() == ListingType.PRODUCT).collect(Collectors.toCollection(ArrayList::new));
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
        return "Product";
    }

    public void createProduct() {
        if (binding.inputName.getText() == null || binding.inputDescription.getText() == null || binding.inputSuggestionCategory.getText() == null
                || binding.inputCategoryDescription.getText() == null || binding.inputPrice.getText() == null || binding.inputDiscount.getText() == null) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        String name = binding.inputName.getText().toString().trim();
        Boolean isAvailable = (Boolean) binding.inputAvailability.getSelected();
        Boolean isPrivate = (Boolean) binding.inputVisibility.getSelected();
        String description = binding.inputDescription.getText().toString().trim();
        String suggestedCategory = binding.inputSuggestionCategory.getText().toString().trim();
        String suggestedCategoryDescription = binding.inputCategoryDescription.getText().toString().trim();

        @SuppressWarnings("unchecked")
        CustomDropDown<ListingCategory> categoryDropdown = binding.inputProductCategory;
        String productCategoryId = categoryDropdown.getSelected() == null ? null : categoryDropdown.getSelected().getId();
        @SuppressWarnings("unchecked")
        ArrayList<EventType> availableEventTypes = binding.inputEventTypes.getSelected();

        // Input Validation
        if (name.isBlank() || isAvailable == null || isPrivate == null || description.isBlank() || availableEventTypes == null || availableEventTypes.isEmpty()
                || ((productCategoryId == null || productCategoryId.isBlank()) && (suggestedCategory.isBlank() || suggestedCategoryDescription.isBlank()))) {
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
            return;
        }

        if (imageFiles.isEmpty()) {
            SingleToast.show(requireContext(), "Please add at least one image");
            return;
        }

        if (suggestedCategory.isBlank()) {
            suggestedCategory = null;
            suggestedCategoryDescription = null;
        } else
            productCategoryId = null;

        productsViewModel.createProduct(imageFiles, TokenManager.getUserId(binding.getRoot().getContext()), name, isAvailable, price, salePercentage, productCategoryId,
                isPrivate, description, suggestedCategory, suggestedCategoryDescription, availableEventTypeIds).observe(getViewLifecycleOwner(), product -> {
            getParentFragmentManager().popBackStack();
        });
    }
}