package com.wde.eventplanner.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardListingCategoryActiveBinding;
import com.wde.eventplanner.fragments.admin.listing_categories.EditListingCategoryDialogFragment;
import com.wde.eventplanner.fragments.admin.listing_categories.ListingCategoriesFragment;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

import java.util.List;

public class ListingCategoryActiveListAdapter extends RecyclerView.Adapter<ListingCategoryActiveListAdapter.ListingCategoryActiveListAdapterHolder> {
    private final List<ListingCategoryDTO> activeListingCategories;
    private final ListingCategoriesFragment parentFragment;

    public ListingCategoryActiveListAdapter(List<ListingCategoryDTO> activeListingCategories, ListingCategoriesFragment parentFragment) {
        this.activeListingCategories = activeListingCategories;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public ListingCategoryActiveListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardListingCategoryActiveBinding binding = CardListingCategoryActiveBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ListingCategoryActiveListAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingCategoryActiveListAdapterHolder holder, int position) {
        ListingCategoryDTO listingCategoryDTO = activeListingCategories.get(position);
        holder.binding.activeListingCategoryTitleTextView.setText(listingCategoryDTO.getName());
        holder.binding.activeListingCategoryDescriptionTextView.setText(listingCategoryDTO.getDescription());

        if (listingCategoryDTO.getIsDeleted()) {
            holder.binding.activeListingCategoryTitleTextView.setTextColor(Color.GRAY);
            holder.binding.activeListingCategoryDescriptionTextView.setTextColor(Color.GRAY);
            holder.binding.editButton.setColorFilter(Color.GRAY);
            holder.binding.deleteButton.setColorFilter(Color.GRAY);
            holder.binding.deleteButton.setEnabled(false);
            holder.binding.editButton.setEnabled(false);
        }

        EditListingCategoryDialogFragment editDialog = new EditListingCategoryDialogFragment(listingCategoryDTO, parentFragment, true);

        holder.binding.editButton.setOnClickListener(v -> editDialog.show(parentFragment.getParentFragmentManager(), "editDialog"));
        holder.binding.deleteButton.setOnClickListener(v -> parentFragment.deleteActiveListing(listingCategoryDTO));
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
