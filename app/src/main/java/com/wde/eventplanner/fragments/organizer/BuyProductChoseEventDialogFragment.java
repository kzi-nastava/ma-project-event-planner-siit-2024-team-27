package com.wde.eventplanner.fragments.organizer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.databinding.DialogBuyProductChoseEventBinding;
import com.wde.eventplanner.models.event.EventComplexView;
import com.wde.eventplanner.models.productBudgetItem.BuyProductDTO;
import com.wde.eventplanner.models.productBudgetItem.ProductBudgetItemDTO;
import com.wde.eventplanner.models.products.Product;
import com.wde.eventplanner.viewmodels.EventsViewModel;
import com.wde.eventplanner.viewmodels.ProductBudgetItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BuyProductChoseEventDialogFragment extends DialogFragment {
    private ProductBudgetItemViewModel productBudgetItemViewModel;
    private DialogBuyProductChoseEventBinding binding;
    private final Product product;

    public BuyProductChoseEventDialogFragment(Product product) {
        this.product = product;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogBuyProductChoseEventBinding.inflate(inflater, container, false);
        EventsViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        productBudgetItemViewModel = new ViewModelProvider(requireActivity()).get(ProductBudgetItemViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getDecorView().setOnTouchListener(binding.eventDropdown::onTouchOutsideDropDown);
        }

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.buyButton.setOnClickListener(v -> buyProduct());
        binding.eventDropdown.disableAutoComplete();

        viewModel.fetchEventsFromOrganizer(TokenManager.getUserId(requireContext()).toString());
        viewModel.getEventsComplexView().observe(getViewLifecycleOwner(), this::onEventsChanged);

        return binding.getRoot();
    }

    private void onEventsChanged(ArrayList<EventComplexView> events) {
        @SuppressWarnings("unchecked")
        CustomDropDown<EventComplexView> eventDropDown = binding.eventDropdown;
        eventDropDown.changeValues(events, EventComplexView::getName, event -> {
            Log.i("", event.toString());

            List<ProductBudgetItemDTO> matchingEvents = event.getProductBudgetItems()
                    .stream()
                    .filter(pbi -> pbi.getProductId() == null)
                    .filter(pbi -> pbi.getProductCategoryId().equals(product.getProductCategoryId()))
                    .filter(pbi -> pbi.getMaxPrice() >= product.getPrice())
                    .collect(Collectors.toList());

            Log.i("", matchingEvents.toString());

            boolean looselyFittingCategory = event.getProductBudgetItems()
                    .stream()
                    .noneMatch(pbi -> pbi.getProductCategoryId().equals(product.getProductCategoryId()));

            Log.i("", String.valueOf(looselyFittingCategory));

            List<UUID> matchingEventTypes = product.getAvailableEventTypeIds()
                    .stream()
                    .filter(et -> et.equals(event.getEventTypeId()))
                    .collect(Collectors.toList());
            Log.i("", matchingEventTypes.toString());

            return (matchingEvents.size() == 1 || looselyFittingCategory) && matchingEventTypes.size() == 1;
        });
    }

    private void buyProduct() {
        EventComplexView chosenEvent = (EventComplexView) binding.eventDropdown.getSelected();
        if (chosenEvent != null) {
            ProductBudgetItemDTO fullfilledProductBudgetItemDTO = chosenEvent.getProductBudgetItems()
                    .stream()
                    .filter(pbi -> pbi.getProductCategoryId().equals(product.getProductCategoryId()))
                    .findFirst()
                    .orElse(null);

            BuyProductDTO buyProductDTO;
            if (fullfilledProductBudgetItemDTO == null) {
                buyProductDTO = new BuyProductDTO(
                        chosenEvent.getId(),
                        product.getStaticProductId()
                );
            } else {
                buyProductDTO = new BuyProductDTO(
                        chosenEvent.getId(),
                        product.getStaticProductId(),
                        fullfilledProductBudgetItemDTO.getProductCategoryId()
                );
            }

            productBudgetItemViewModel.buyProduct(buyProductDTO);
        }

        dismiss();
    }
}
