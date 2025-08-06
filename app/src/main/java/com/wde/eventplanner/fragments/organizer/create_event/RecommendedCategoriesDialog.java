package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.StringAdapter;
import com.wde.eventplanner.databinding.DialogRecommendedCategoriesForEventTypeBinding;
import com.wde.eventplanner.models.event.RecommendedListingCategoriesDTO;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;

import java.util.stream.Collectors;

public class RecommendedCategoriesDialog extends DialogFragment {
    private DialogRecommendedCategoriesForEventTypeBinding binding;
    private final String eventTypeId;

    public RecommendedCategoriesDialog(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogRecommendedCategoriesForEventTypeBinding.inflate(inflater, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EventTypesViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);

        viewModel.fetchRecommendedListingCategoriesForEventType(eventTypeId).observe(
                getViewLifecycleOwner(), this::populateRecommendedTypes);

        binding.closeButton.setOnClickListener(v -> dismiss());

        return binding.getRoot();
    }

    private void populateRecommendedTypes(RecommendedListingCategoriesDTO recommendedListingCategoriesDTO) {
        binding.recommendedProductCategories.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recommendedServiceCategories.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        binding.recommendedProductCategories.setAdapter(
                new StringAdapter(
                        recommendedListingCategoriesDTO.getProductCategories()
                                .stream()
                                .map(ListingCategory::getName).collect(Collectors.toList())
                )
        );

        binding.recommendedServiceCategories.setAdapter(
                new StringAdapter(
                        recommendedListingCategoriesDTO.getServiceCategories()
                                .stream()
                                .map(ListingCategory::getName).collect(Collectors.toList())
                )
        );
    }
}
