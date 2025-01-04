package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.BudgetItemAdapter;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.databinding.FragmentEventBudgetBinding;
import com.wde.eventplanner.models.event.ListingBudgetItemDTO;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class EventBudgetFragment extends Fragment implements ViewPagerAdapter.HasTitle, BudgetItemAdapter.BudgetItemAdapterCallback {
    private FragmentEventBudgetBinding binding;
    private ListingCategoriesViewModel listingCategoriesViewModel;
    private ArrayList<ListingBudgetItemDTO> budgetItems;
    private ArrayList<ListingCategory> listingCategories;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventBudgetBinding.inflate(inflater, container, false);

        budgetItems = new ArrayList<>();

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        listingCategoriesViewModel = viewModelProvider.get(ListingCategoriesViewModel.class);
        listingCategoriesViewModel.fetchActiveListingCategories();

        // todo temp event id, when other steps are over, put it in the constructor and pass it here
        RecommendedCategoriesDialog recommendedCategoriesDialog = new RecommendedCategoriesDialog("a8b8d5b9-d1b2-47e1-b5a6-3efac3b6b832");
        binding.recommendedCategoriesButton.setOnClickListener(
                v -> recommendedCategoriesDialog.show(getParentFragmentManager(), "recommendedCategoriesDialog"));

        binding.listingBudgetItemsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.listingBudgetItemsRecyclerView.setNestedScrollingEnabled(false);

        binding.addCategoryButton.setOnClickListener(v -> {
            if (budgetItems
                    .stream()
                    .allMatch(pbi -> pbi.getListingCategoryId() != null &&
                            pbi.getListingType() != null &&
                            pbi.getMaxPrice() != null)) {
                budgetItems.add(new ListingBudgetItemDTO());
                binding.listingBudgetItemsRecyclerView.getAdapter().notifyItemChanged(budgetItems.size() - 1);

            } else {
                SingleToast.show(requireContext(), "Fill out all previous items");
            }
        });

        listingCategoriesViewModel.getActiveListingCategories().observe(getViewLifecycleOwner(), (activeCategories) ->
            binding.listingBudgetItemsRecyclerView.setAdapter(new BudgetItemAdapter(budgetItems, activeCategories, this)));

        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "Budget";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void changeFilter() {
        for (int i = 0; i < binding.listingBudgetItemsRecyclerView.getAdapter().getItemCount(); i++) {
            // get holder of current card (the holder contains dropdowns that need to be reset)
            BudgetItemAdapter.BudgetItemViewHolder holder =
                    (BudgetItemAdapter.BudgetItemViewHolder) binding.listingBudgetItemsRecyclerView
                            .findViewHolderForAdapterPosition(i);

            if (holder != null) {
                int j = i;
                // reset category dropdown filter to
                ((CustomDropDown<ListingCategory>) holder.binding.categoryDropdown).changeFilter(
                        c -> {
                            ListingBudgetItemDTO relativeBudgetItem = budgetItems.get(j);
                            // match listing types of the current budget item and it's listing type selector
                            return c.getListingType() == relativeBudgetItem.getListingType()
                                    && (budgetItems
                                    .stream()
                                    .noneMatch(pbi ->
                                            // and to prevent two categories from being selected in different cards
                                            pbi.getListingCategoryId() != null &&
                                                    pbi.getListingCategoryId().equals(c.getId()) &&
                                                    !pbi.getListingCategoryId().equals(relativeBudgetItem.getListingCategoryId())));
                        }
                );
            }
        }
    }

    @Override
    public void totalAmount() {
        binding.totalAmountTextBox
                .setText(String.format(
                        Locale.ENGLISH,
                        "Total amount: %.2f€",
                        budgetItems.stream()
                                .mapToDouble(pbi -> pbi.getMaxPrice() == null ? 0 : pbi.getMaxPrice())
                                .sum())
                );
    }
}