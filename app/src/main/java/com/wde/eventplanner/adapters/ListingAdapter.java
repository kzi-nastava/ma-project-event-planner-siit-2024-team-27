package com.wde.eventplanner.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.models.user.UserRole;
import com.wde.eventplanner.utils.MenuManager;
import com.wde.eventplanner.databinding.CardListingBinding;
import com.wde.eventplanner.models.listing.Listing;
import com.wde.eventplanner.models.listing.ListingType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {
    private final NavController navController;
    public final List<Listing> listings;
    private final boolean isHomeScreen;

    public ListingAdapter(NavController navController, boolean isHomeScreen) {
        this.navController = navController;
        this.listings = new ArrayList<>();
        this.isHomeScreen = isHomeScreen;
    }

    public ListingAdapter(List<Listing> listings, NavController navController, boolean isHomeScreen) {
        this.navController = navController;
        this.isHomeScreen = isHomeScreen;
        this.listings = listings;
    }

    public ListingAdapter(NavController navController) {
        this.navController = navController;
        this.listings = new ArrayList<>();
        this.isHomeScreen = false;
    }

    public ListingAdapter(List<Listing> listings, NavController navController) {
        this.navController = navController;
        this.isHomeScreen = false;
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
            String id = listing.getId();
            String type = listing.getType().toString();
            String version = listing.getVersion().toString();
            Context context = holder.binding.getRoot().getContext();
            UserRole role = isHomeScreen ? UserRole.ANONYMOUS : null;
            holder.binding.cardView.setOnClickListener(v -> MenuManager.navigateToFragment(type, id, version, context, navController, role));
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