package com.wde.eventplanner.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.databinding.CardEventBudgetItemBinding;
import com.wde.eventplanner.models.event.ListingBudgetItemDTO;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.models.user.UserRole;
import com.wde.eventplanner.utils.MenuManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder> {
    public interface BudgetItemAdapterCallback {
        void changeFilter();

        void totalAmount();
    }

    private final List<ListingCategory> listingCategories;
    private final List<ListingBudgetItemDTO> budgetItems;
    private final BudgetItemAdapterCallback budgetItemAdapterCallback;
    private final NavController navController;

    public BudgetItemAdapter(List<ListingBudgetItemDTO> budgetItems,
                             List<ListingCategory> listingCategories,
                             BudgetItemAdapterCallback budgetItemAdapterCallback,
                             NavController navController) {
        this.budgetItems = budgetItems;
        this.listingCategories = listingCategories;
        this.budgetItemAdapterCallback = budgetItemAdapterCallback;
        this.navController = navController;
    }

    @NonNull
    @Override
    public BudgetItemAdapter.BudgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardEventBudgetItemBinding binding = CardEventBudgetItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BudgetItemViewHolder(binding);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull BudgetItemViewHolder holder, int position) {
        ListingBudgetItemDTO budgetItem = budgetItems.get(position);
        CustomDropDown<ListingCategory> categoryDropdown = holder.binding.categoryDropdown;
        CustomDropDown<ListingType> typeDropdown = holder.binding.typeDropdown;

        holder.binding.typeDropdown.setEnabled(budgetItem.getId() == null);

        holder.binding.deleteButton.setEnabled(budgetItem.getListingId() == null);
        holder.binding.navigateToDetailButton.setEnabled(budgetItem.getListingId() != null);
        holder.binding.budgetTextbox.setEnabled(budgetItem.getListingId() == null);

        holder.binding.budgetTextbox.setText(budgetItem.getMaxPrice() != null ? budgetItem.getMaxPrice().toString() : "");

        typeDropdown.clearItems();

        categoryDropdown.clearItems();
        holder.binding.categoryDropdown.setEnabled(false);

        holder.binding.deleteButton.setOnClickListener(v -> {
            int dynamicPosition = holder.getAdapterPosition();
            budgetItems.remove(dynamicPosition);
            holder.binding.deleteButton.setOnClickListener(Function.identity()::apply);
            notifyItemRemoved(dynamicPosition);
            budgetItemAdapterCallback.totalAmount();
            budgetItemAdapterCallback.changeFilter();
        });

        holder.binding.navigateToDetailButton.setOnClickListener(v->{
            MenuManager.navigateToFragment(budgetItem.getListingType().toString().toUpperCase(),
                    budgetItem.getListingId(), budgetItem.getListingVersion().toString(), holder.binding.getRoot().getContext(), navController, UserRole.ANONYMOUS);
        });

        categoryDropdown.changeValues(new ArrayList<>(this.listingCategories), ListingCategory::getName);
        categoryDropdown.setOnDropdownItemClickListener(category -> {
            budgetItem.setListingCategoryId(category.getId());
            budgetItemAdapterCallback.changeFilter();
        });

        typeDropdown.disableAutoComplete();
        typeDropdown.changeValues(new ArrayList<>(Arrays.asList(ListingType.values())), ListingType::toString);
        typeDropdown.setOnDropdownItemClickListener(type -> {
            budgetItem.setListingType(type);
            budgetItem.setListingCategoryId(null);
            budgetItemAdapterCallback.changeFilter();
            categoryDropdown.setEnabled(true);
        });

        if (budgetItem.getListingType() != null) {
            typeDropdown.setText(budgetItem.getListingType().toString());
            budgetItemAdapterCallback.changeFilter();
            categoryDropdown.setEnabled(true);
        }

        if (budgetItem.getListingCategoryId() != null) {
            IntStream.range(0, listingCategories.size())
                    .filter(i -> listingCategories.get(i).getId().equals(budgetItem.getListingCategoryId()))
                    .findFirst().ifPresent(categoryDropdown::setSelected);
            budgetItemAdapterCallback.changeFilter();
        }

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
        holder.binding.categoryDropdown.setEnabled(budgetItem.getId() == null);
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
