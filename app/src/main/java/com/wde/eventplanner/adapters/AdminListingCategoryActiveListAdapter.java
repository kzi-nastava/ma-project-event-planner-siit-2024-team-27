package com.wde.eventplanner.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardListingCategoryActiveBinding;
import com.wde.eventplanner.fragments.AdminListingCategories.AdminListingCategoriesFragment;
import com.wde.eventplanner.fragments.AdminListingCategories.EditListingCategoryDialogFragment;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

import java.util.List;

public class AdminListingCategoryActiveListAdapter extends RecyclerView.Adapter<AdminListingCategoryActiveListAdapter.AdminListingCategoryActiveListAdapterHolder> {
    private List<ListingCategoryDTO> activeListingCategories;
    private AdminListingCategoriesFragment parentFragment;

    public AdminListingCategoryActiveListAdapter(List<ListingCategoryDTO> activeListingCategories, AdminListingCategoriesFragment parentFragment) {
        this.activeListingCategories = activeListingCategories;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public AdminListingCategoryActiveListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardListingCategoryActiveBinding binding = CardListingCategoryActiveBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminListingCategoryActiveListAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListingCategoryActiveListAdapterHolder holder, int position) {
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

    public static class AdminListingCategoryActiveListAdapterHolder extends RecyclerView.ViewHolder {
        CardListingCategoryActiveBinding binding;

        public AdminListingCategoryActiveListAdapterHolder(CardListingCategoryActiveBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
