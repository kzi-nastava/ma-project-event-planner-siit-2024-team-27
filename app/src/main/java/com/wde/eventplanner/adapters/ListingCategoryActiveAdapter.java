package com.wde.eventplanner.adapters;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardListingCategoryActiveBinding;
import com.wde.eventplanner.fragments.admin.listing_categories.EditListingCategoryDialogFragment;
import com.wde.eventplanner.fragments.admin.listing_categories.ListingCategoriesFragment;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListingCategoryActiveAdapter extends RecyclerView.Adapter<ListingCategoryActiveAdapter.ListingCategoryActiveListAdapterHolder> {
    public final List<ListingCategory> activeListingCategories;
    private final ListingCategoriesFragment parent;
    private ListingCategoriesViewModel viewModel;

    public ListingCategoryActiveAdapter(ListingCategoriesFragment parent) {
        this.activeListingCategories = new ArrayList<>();
        this.parent = parent;
    }

    public ListingCategoryActiveAdapter(List<ListingCategory> activeListingCategories, ListingCategoriesFragment parent) {
        this.activeListingCategories = activeListingCategories;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ListingCategoryActiveListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardListingCategoryActiveBinding binding = CardListingCategoryActiveBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        viewModel = new ViewModelProvider(this.parent.requireActivity()).get(ListingCategoriesViewModel.class);
        return new ListingCategoryActiveListAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingCategoryActiveListAdapterHolder holder, int position) {
        ListingCategory listingCategory = activeListingCategories.get(position);
        holder.binding.activeListingCategoryTitleTextView.setText(listingCategory.getName());
        holder.binding.activeListingCategoryDescriptionTextView.setText(listingCategory.getDescription());

        int textColor = listingCategory.getIsDeleted() ? Color.GRAY : Color.WHITE;
        ColorFilter filterColor = listingCategory.getIsDeleted() ? new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN) : null;
        holder.binding.editButton.setColorFilter(filterColor);
        holder.binding.deleteButton.setColorFilter(filterColor);
        holder.binding.activeListingCategoryTitleTextView.setTextColor(textColor);
        holder.binding.activeListingCategoryDescriptionTextView.setTextColor(textColor);
        holder.binding.deleteButton.setEnabled(!listingCategory.getIsDeleted());
        holder.binding.editButton.setEnabled(!listingCategory.getIsDeleted());

        EditListingCategoryDialogFragment editDialog = new EditListingCategoryDialogFragment(listingCategory, true);

        holder.binding.editButton.setOnClickListener(v -> editDialog.show(parent.getParentFragmentManager(), "editDialog"));
        holder.binding.deleteButton.setOnClickListener(v -> viewModel.deleteActiveListingCategory(listingCategory));
    }

    @Override
    public int getItemCount() {
        return activeListingCategories.size();
    }

    public static class ListingCategoryActiveListAdapterHolder extends RecyclerView.ViewHolder {
        CardListingCategoryActiveBinding binding;

        public ListingCategoryActiveListAdapterHolder(CardListingCategoryActiveBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
