package com.wde.eventplanner.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.models.Listing;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.EventViewHolder> {

    private List<Listing> listingList;

    public ListingAdapter(List<Listing> listingList) {
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
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, originalPriceTextView, priceTextView, ratingTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find the views in the layout
            titleTextView = itemView.findViewById(R.id.listingCardTitle);
            originalPriceTextView = itemView.findViewById(R.id.listingCardOriginalPrice);
            originalPriceTextView.setPaintFlags(originalPriceTextView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            priceTextView = itemView.findViewById(R.id.listingCardPrice);
            ratingTextView = itemView.findViewById(R.id.listingCardRating);
        }
    }
}