package com.wde.eventplanner.adapters;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.CardListingBinding;
import com.wde.eventplanner.models.listing.Listing;
import com.wde.eventplanner.models.listing.ListingType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {
    private final NavController navController;
    public final List<Listing> listings;

    public ListingAdapter() {
        this.listings = new ArrayList<>();
        navController = null;
    }

    public ListingAdapter(NavController navController) {
        this.navController = navController;
        this.listings = new ArrayList<>();
    }

    public ListingAdapter(List<Listing> listings) {
        this.listings = listings;
        navController = null;
    }

    public ListingAdapter(List<Listing> listings, NavController navController) {
        this.navController = navController;
        this.listings = listings;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardListingBinding binding = CardListingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ListingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listings.get(position);
        Picasso.get().load(listing.getImages().get(0)).into(holder.binding.listingCardPicture);
        holder.binding.listingCardTitle.setText(listing.getName());
        String priceEnding = listing.getType() == ListingType.SERVICE ? "€/hr" : "€";
        if (listing.getOldPrice() != null)
            holder.binding.listingCardOldPrice.setText(String.format(Locale.ENGLISH, "%.2f%s", listing.getOldPrice(), priceEnding));
        holder.binding.listingCardOldPrice.setPaintFlags(holder.binding.listingCardOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.listingCardPrice.setText(String.format(Locale.ENGLISH, "%.2f%s", listing.getPrice(), priceEnding));
        holder.binding.listingCardRating.setText(String.format(Locale.ENGLISH, "%2.1f", listing.getRating()));

        if (navController != null) {
            // todo dynamic fragment by user role
            Bundle bundle = new Bundle();
            bundle.putString("staticId", listing.getId());
            bundle.putInt("version", listing.getVersion());
            if (listing.getType() == ListingType.SERVICE)
                holder.binding.cardView.setOnClickListener(v -> navController.navigate(R.id.ServiceDetailOrganizerFragment, bundle));
            else
                holder.binding.cardView.setOnClickListener(v -> navController.navigate(R.id.ProductDetailOrganizerFragment, bundle));
        }
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class ListingViewHolder extends RecyclerView.ViewHolder {
        CardListingBinding binding;

        public ListingViewHolder(CardListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}