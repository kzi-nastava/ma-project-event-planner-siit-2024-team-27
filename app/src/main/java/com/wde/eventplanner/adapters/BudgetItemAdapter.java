package com.wde.eventplanner.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.databinding.CardEventBudgetItemBinding;
import com.wde.eventplanner.models.event.ListingBudgetItemDTO;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder> {
    public interface BudgetItemAdapterCallback {
        void changeFilter();

        void totalAmount();
    }

    private List<ListingCategory> listingCategories;
    private List<ListingBudgetItemDTO> budgetItems;
    private BudgetItemAdapterCallback budgetItemAdapterCallback;

    public BudgetItemAdapter(List<ListingBudgetItemDTO> budgetItems,
                             List<ListingCategory> listingCategories,
                             BudgetItemAdapterCallback budgetItemAdapterCallback) {
        this.budgetItems = budgetItems;
        this.listingCategories = listingCategories;
        this.budgetItemAdapterCallback = budgetItemAdapterCallback;
    }

    @NonNull
    @Override
    public BudgetItemAdapter.BudgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardEventBudgetItemBinding binding = CardEventBudgetItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BudgetItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetItemViewHolder holder, int position) {
        ListingBudgetItemDTO budgetItem = budgetItems.get(position);
        CustomDropDown<ListingCategory> categoryDropdown = holder.binding.categoryDropdown;
        CustomDropDown<ListingType> typeDropdown = holder.binding.typeDropdown;

        holder.binding.budgetTextbox.setText("");

        typeDropdown.clearItems();

        categoryDropdown.clearItems();
        holder.binding.categoryDropdown.setEnabled(false);

        holder.binding.deleteButton.setOnClickListener(v -> {
            int dynamicPosition = holder.getAdapterPosition();
            budgetItems.remove(dynamicPosition);
            holder.binding.deleteButton.setOnClickListener(_v -> {
            });
            notifyItemRemoved(dynamicPosition);
            budgetItemAdapterCallback.totalAmount();
            budgetItemAdapterCallback.changeFilter();
        });

        categoryDropdown.changeValues(new ArrayList<>(this.listingCategories), ListingCategory::getName);
        categoryDropdown.setOnDropdownItemClickListener(() -> {
            budgetItem.setListingCategoryId(categoryDropdown.getSelected().getId());
            budgetItemAdapterCallback.changeFilter();
        });

        typeDropdown.disableAutoComplete();
        typeDropdown.changeValues(new ArrayList<>(Arrays.asList(ListingType.values())), ListingType::toString);
        typeDropdown.setOnDropdownItemClickListener(() -> {
            budgetItem.setListingType(typeDropdown.getSelected());
            budgetItem.setListingCategoryId(null);
            budgetItemAdapterCallback.changeFilter();
            categoryDropdown.setEnabled(true);
        });

        if (holder.textWatcher != null)
            holder.binding.budgetTextbox.removeTextChangedListener(holder.textWatcher);

        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty())
                    budgetItem.setMaxPrice(Double.parseDouble(s.toString()));
                else
                    budgetItem.setMaxPrice(0.0);
                budgetItemAdapterCallback.totalAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        holder.binding.budgetTextbox.addTextChangedListener(holder.textWatcher);
    }

    @Override
    public int getItemCount() {
        return budgetItems.size();
    }

    public static class BudgetItemViewHolder extends RecyclerView.ViewHolder {
        public CardEventBudgetItemBinding binding;
        public TextWatcher textWatcher;

        public BudgetItemViewHolder(CardEventBudgetItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
