package com.wde.eventplanner.adapters;

import android.graphics.Color;
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
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

import java.util.List;

public class AdminListingCategoryActiveListAdapter extends RecyclerView.Adapter<AdminListingCategoryActiveListAdapter.AdminListingCategoryActiveListAdapterHolder> {
    private List<ListingCategoryDTO> activeListingCategories;
    private AdminListingCategoriesFragment parentFragment;

    public AdminListingCategoryActiveListAdapter(List<ListingCategoryDTO> activeListingCategories,
                                                 AdminListingCategoriesFragment parentFragment) {
        this.activeListingCategories = activeListingCategories;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public AdminListingCategoryActiveListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_listing_category_active, parent, false);
        return new AdminListingCategoryActiveListAdapter.AdminListingCategoryActiveListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListingCategoryActiveListAdapterHolder holder, int position) {
        ListingCategoryDTO listingCategoryDTO = activeListingCategories.get(position);
        holder.listingCategoryTitleTextView.setText(listingCategoryDTO.getName());
        holder.listingCategoryDescriptionTextView.setText(listingCategoryDTO.getDescription());

        if (listingCategoryDTO.getIsDeleted()) {
            holder.listingCategoryTitleTextView.setTextColor(Color.GRAY);
            holder.listingCategoryDescriptionTextView.setTextColor(Color.GRAY);
            holder.editImageButton.setColorFilter(Color.GRAY);
            holder.deleteImageButton.setColorFilter(Color.GRAY);
            holder.deleteImageButton.setEnabled(false);
            holder.editImageButton.setEnabled(false);
        }

        EditListingCategoryDialogFragment editDialog = new EditListingCategoryDialogFragment(listingCategoryDTO, parentFragment, true);

        holder.editImageButton.setOnClickListener(v -> editDialog.show(parentFragment.getParentFragmentManager(), "editDialog"));
        holder.deleteImageButton.setOnClickListener(v -> parentFragment.deleteActiveListing(listingCategoryDTO));
    }

    @Override
    public int getItemCount() {
        return activeListingCategories.size();
    }

    public static class AdminListingCategoryActiveListAdapterHolder extends RecyclerView.ViewHolder {
        TextView listingCategoryTitleTextView, listingCategoryDescriptionTextView;
        CardView listingCategoryActiveCard;
        ImageButton editImageButton, deleteImageButton;

        public AdminListingCategoryActiveListAdapterHolder(@NonNull View itemView) {
            super(itemView);

            listingCategoryTitleTextView = itemView.findViewById(R.id.activeListingCategoryTitleTextView);
            listingCategoryDescriptionTextView = itemView.findViewById(R.id.activeListingCategoryDescriptionTextView);
            editImageButton = itemView.findViewById(R.id.editButton);
            deleteImageButton = itemView.findViewById(R.id.deleteButton);
            listingCategoryActiveCard = itemView.findViewById(R.id.listingCategoryActiveCard);
        }
    }
}
