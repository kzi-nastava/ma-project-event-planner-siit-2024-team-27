package com.wde.eventplanner.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.CardListingBinding;
import com.wde.eventplanner.models.Listing;
import com.wde.eventplanner.models.ListingType;

import java.util.List;
import java.util.Locale;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {

    private List<Listing> listingList;
    private NavController navController;

    public ListingAdapter(List<Listing> listingList) {
        this.listingList = listingList;
        navController = null;
    }

    public ListingAdapter(List<Listing> listingList, NavController navController) {
        this.navController = navController;
        this.listingList = listingList;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardListingBinding binding = CardListingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ListingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listingList.get(position);
        Picasso.get().load(listing.getImages().get(0)).into(holder.binding.listingCardPicture);
        holder.binding.listingCardTitle.setText(listing.getName());
        String priceEnding = listing.getType() == ListingType.SERVICE ? "€/hr" : "€";
        if (listing.getOldPrice() != null)
            holder.binding.listingCardOldPrice.setText(String.format(Locale.ENGLISH, "%.2f%s", listing.getOldPrice(), priceEnding));
        holder.binding.listingCardOldPrice.setPaintFlags(holder.binding.listingCardOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.listingCardPrice.setText(String.format(Locale.ENGLISH, "%.2f%s", listing.getPrice(), priceEnding));
        holder.binding.listingCardRating.setText(String.format(Locale.ENGLISH, "%2.1f", listing.getRating()));

        if (navController != null)
            holder.binding.cardView.setOnClickListener(v -> navController.navigate(R.id.SellerListingDetailFragment));
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public static class ListingViewHolder extends RecyclerView.ViewHolder {
        CardListingBinding binding;

        public ListingViewHolder(CardListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}