package com.wde.eventplanner.fragments.admin.event_categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.R;
import com.wde.eventplanner.components.MultiDropDown;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.databinding.DialogEditEventTypeBinding;
import com.wde.eventplanner.models.event.EventType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EditEventTypeDialogFragment extends DialogFragment {
    private ListingCategoriesViewModel listingCategoriesViewModel;
    private EventTypesViewModel eventTypeViewModel;
    private DialogEditEventTypeBinding binding;
    private EventType eventType;

    public EditEventTypeDialogFragment(EventType eventType) {
        this.eventType = eventType;
    }

    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditEventTypeBinding.inflate(inflater, container, false);
        listingCategoriesViewModel = new ViewModelProvider(requireActivity()).get(ListingCategoriesViewModel.class);
        eventTypeViewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener(binding.categoriesDropdown::onTouchOutsideDropDown);
        }

        if (eventType != null) {
            binding.titleView.setText(R.string.edit_type);
            binding.name.setText(eventType.getName());
            binding.description.setText(eventType.getDescription());
            binding.submitButton.setText(R.string.edit);
        }

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.submitButton.setOnClickListener(v -> submitEventType());

        listingCategoriesViewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), this::onCategoriesChanged);
        listingCategoriesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                listingCategoriesViewModel.clearErrorMessage();
            }
        });
        listingCategoriesViewModel.fetchActiveListingCategories();

        return binding.getRoot();
    }

    @SuppressWarnings("unchecked")
    private void onCategoriesChanged(ArrayList<ListingCategory> categories) {
        MultiDropDown<ListingCategory> categoriesDropdown = binding.categoriesDropdown;
        categoriesDropdown.changeValues(categories, ListingCategory::getName);
        if (eventType != null)
            binding.categoriesDropdown.setSelected(eventType.getListingCategories());
    }

    @SuppressWarnings("unchecked")
    private void submitEventType() {
        if (binding.name.getText() != null && binding.description.getText() != null) {
            String name = binding.name.getText().toString();
            String description = binding.description.getText().toString();
            MultiDropDown<ListingCategory> dropDown = (MultiDropDown<ListingCategory>) binding.categoriesDropdown;
            if (eventType != null)
                eventTypeViewModel.editEventType(eventType.getId(), new EventType(eventType.getId(), name, description, true, dropDown.getSelected()));
            else
                eventTypeViewModel.createEventType(new EventType(null, name, description, true, dropDown.getSelected()));
        }
        dismiss();
    }
}
