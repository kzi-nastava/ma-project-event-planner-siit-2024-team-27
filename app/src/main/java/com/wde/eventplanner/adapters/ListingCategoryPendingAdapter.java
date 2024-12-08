package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardListingCategoryPendingBinding;
import com.wde.eventplanner.fragments.admin.listing_categories.EditListingCategoryDialogFragment;
import com.wde.eventplanner.fragments.admin.listing_categories.ListingCategoriesFragment;
import com.wde.eventplanner.fragments.admin.listing_categories.ReplaceListingCategoryDialogFragment;
import com.wde.eventplanner.models.listingCategory.ListingCategory;
import com.wde.eventplanner.viewmodels.ListingCategoriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListingCategoryPendingAdapter extends RecyclerView.Adapter<ListingCategoryPendingAdapter.ListingCategoryPendingListAdapterHolder> {
    public final List<ListingCategory> pendingListingCategories;
    private final ListingCategoriesFragment parent;
    private ListingCategoriesViewModel viewModel;

    public ListingCategoryPendingAdapter(ListingCategoriesFragment parent) {
        this.pendingListingCategories = new ArrayList<>();
        this.parent = parent;
    }

    public ListingCategoryPendingAdapter(List<ListingCategory> activeListingCategories, ListingCategoriesFragment parent) {
        this.pendingListingCategories = activeListingCategories;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ListingCategoryPendingListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardListingCategoryPendingBinding binding = CardListingCategoryPendingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        viewModel = new ViewModelProvider(this.parent.requireActivity()).get(ListingCategoriesViewModel.class);
        return new ListingCategoryPendingListAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingCategoryPendingListAdapterHolder holder, int position) {
        ListingCategory listingCategory = pendingListingCategories.get(position);
        holder.binding.pendingListingCategoryTitleTextView.setText(listingCategory.getName());

        EditListingCategoryDialogFragment editDialog = new EditListingCategoryDialogFragment(listingCategory, false);
        ReplaceListingCategoryDialogFragment replaceDialog = new ReplaceListingCategoryDialogFragment(listingCategory);

        holder.binding.replaceButton.setOnClickListener(v -> replaceDialog.show(parent.getParentFragmentManager(), "replaceDialog"));
        holder.binding.editButton.setOnClickListener(v -> editDialog.show(parent.getParentFragmentManager(), "editDialog"));
        holder.binding.approveButton.setOnClickListener(v -> viewModel.approvePendingListingCategory(listingCategory));
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
