package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardListingCategoryPendingBinding;
import com.wde.eventplanner.fragments.AdminListingCategories.AdminListingCategoriesFragment;
import com.wde.eventplanner.fragments.AdminListingCategories.EditListingCategoryDialogFragment;
import com.wde.eventplanner.fragments.AdminListingCategories.ReplaceListingCategoryDialogFragment;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

import java.util.List;

public class AdminListingCategoryPendingListAdapter extends RecyclerView.Adapter<AdminListingCategoryPendingListAdapter.AdminListingCategoryPendingListAdapterHolder> {
    private List<ListingCategoryDTO> pendingListingCategories;
    private List<ListingCategoryDTO> activeListingCategories;
    private AdminListingCategoriesFragment parentFragment;

    public AdminListingCategoryPendingListAdapter(List<ListingCategoryDTO> pendingListingCategories,
                                                  List<ListingCategoryDTO> activeListingCategories,
                                                  AdminListingCategoriesFragment parentFragment) {
        this.activeListingCategories = activeListingCategories;
        this.pendingListingCategories = pendingListingCategories;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public AdminListingCategoryPendingListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardListingCategoryPendingBinding binding = CardListingCategoryPendingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminListingCategoryPendingListAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListingCategoryPendingListAdapterHolder holder, int position) {
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

    public static class AdminListingCategoryPendingListAdapterHolder extends RecyclerView.ViewHolder {
        final CardListingCategoryPendingBinding binding;

        public AdminListingCategoryPendingListAdapterHolder(CardListingCategoryPendingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
