package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardListingCategoryPendingBinding;
import com.wde.eventplanner.fragments.admin.listing_categories.ListingCategoriesFragment;
import com.wde.eventplanner.fragments.admin.listing_categories.EditListingCategoryDialogFragment;
import com.wde.eventplanner.fragments.admin.listing_categories.ReplaceListingCategoryDialogFragment;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

import java.util.List;

public class ListingCategoryPendingListAdapter extends RecyclerView.Adapter<ListingCategoryPendingListAdapter.ListingCategoryPendingListAdapterHolder> {
    private List<ListingCategoryDTO> pendingListingCategories;
    private List<ListingCategoryDTO> activeListingCategories;
    private ListingCategoriesFragment parentFragment;

    public ListingCategoryPendingListAdapter(List<ListingCategoryDTO> pendingListingCategories,
                                             List<ListingCategoryDTO> activeListingCategories,
                                             ListingCategoriesFragment parentFragment) {
        this.activeListingCategories = activeListingCategories;
        this.pendingListingCategories = pendingListingCategories;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public ListingCategoryPendingListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardListingCategoryPendingBinding binding = CardListingCategoryPendingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ListingCategoryPendingListAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingCategoryPendingListAdapterHolder holder, int position) {
        ListingCategoryDTO listingCategoryDTO = pendingListingCategories.get(position);
        holder.binding.pendingListingCategoryTitleTextView.setText(listingCategoryDTO.getName());
        holder.binding.pendingListingCategoryTypeTextView.setText(listingCategoryDTO.getListingType().toString());

        EditListingCategoryDialogFragment editDialog = new EditListingCategoryDialogFragment(listingCategoryDTO, parentFragment, false);
        ReplaceListingCategoryDialogFragment replaceDialog = new ReplaceListingCategoryDialogFragment(activeListingCategories, listingCategoryDTO, parentFragment);

        holder.binding.replaceButton.setOnClickListener(v -> replaceDialog.show(parentFragment.getParentFragmentManager(), "replaceDialog"));
        holder.binding.approveButton.setOnClickListener(v -> parentFragment.approvePendingListing(listingCategoryDTO));
        holder.binding.editButton.setOnClickListener(v -> editDialog.show(parentFragment.getParentFragmentManager(), "editDialog"));
    }

    @Override
    public int getItemCount() {
        return pendingListingCategories.size();
    }

    public static class ListingCategoryPendingListAdapterHolder extends RecyclerView.ViewHolder {
        final CardListingCategoryPendingBinding binding;

        public ListingCategoryPendingListAdapterHolder(CardListingCategoryPendingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
