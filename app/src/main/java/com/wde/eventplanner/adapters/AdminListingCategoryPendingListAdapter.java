package com.wde.eventplanner.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_listing_category_pending, parent, false);
        return new AdminListingCategoryPendingListAdapter.AdminListingCategoryPendingListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListingCategoryPendingListAdapterHolder holder, int position) {
        ListingCategoryDTO listingCategoryDTO = pendingListingCategories.get(position);
        holder.listingCategoryTitleTextView.setText(listingCategoryDTO.getName());
        holder.listingCategoryTypeTextView.setText(listingCategoryDTO.getListingType().toString());

        EditListingCategoryDialogFragment editDialog = new EditListingCategoryDialogFragment(listingCategoryDTO, parentFragment, false);
        ReplaceListingCategoryDialogFragment replaceDialog = new ReplaceListingCategoryDialogFragment(activeListingCategories,
                listingCategoryDTO, parentFragment);

        holder.replaceImageButton.setOnClickListener(v -> replaceDialog.show(parentFragment.getParentFragmentManager(), "replaceDialog"));
        holder.approveImageButton.setOnClickListener(v -> parentFragment.approvePendingListing(listingCategoryDTO));
        holder.editImageButton.setOnClickListener(v -> editDialog.show(parentFragment.getParentFragmentManager(), "editDialog"));
    }

    @Override
    public int getItemCount() {
        return pendingListingCategories.size();
    }

    public static class AdminListingCategoryPendingListAdapterHolder extends RecyclerView.ViewHolder {
        TextView listingCategoryTitleTextView, listingCategoryTypeTextView;
        CardView listingCategoryPendingCard;
        ImageButton replaceImageButton, editImageButton, approveImageButton;

        public AdminListingCategoryPendingListAdapterHolder(@NonNull View itemView) {
            super(itemView);

            listingCategoryTitleTextView = itemView.findViewById(R.id.pendingListingCategoryTitleTextView);
            listingCategoryTypeTextView = itemView.findViewById(R.id.pendingListingCategoryTypeTextView);
            replaceImageButton = itemView.findViewById(R.id.replaceButton);
            editImageButton = itemView.findViewById(R.id.editButton);
            approveImageButton = itemView.findViewById(R.id.approveButton);
            listingCategoryPendingCard = itemView.findViewById(R.id.listingCategoryPendingCard);
        }
    }
}
