package com.wde.eventplanner.adapters;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.models.Listing;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.EventViewHolder> {

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
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_listing, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Listing listing = listingList.get(position);
        holder.titleTextView.setText(listing.getTitle());
        holder.originalPriceTextView.setText(listing.getOriginalPrice());
        holder.priceTextView.setText(listing.getPrice());
        holder.ratingTextView.setText(String.format("%2.1f", listing.getRating()));

        if (navController != null)
            holder.cardView.setOnClickListener(v -> navController.navigate(R.id.SellerListingDetailFragment));
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, originalPriceTextView, priceTextView, ratingTextView;
        CardView cardView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find the views in the layout
            titleTextView = itemView.findViewById(R.id.listingCardTitle);
            originalPriceTextView = itemView.findViewById(R.id.listingCardOriginalPrice);
            originalPriceTextView.setPaintFlags(originalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceTextView = itemView.findViewById(R.id.listingCardPrice);
            ratingTextView = itemView.findViewById(R.id.listingCardRating);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}