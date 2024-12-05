package com.wde.eventplanner.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_listing, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listingList.get(position);
        Picasso.get().load(listing.getImages().get(0)).into(holder.listingCardPicture);
        holder.titleTextView.setText(listing.getName());
        String priceEnding = listing.getType() == ListingType.SERVICE ? "€/hr" : "€";
        if (listing.getOldPrice() != null)
            holder.oldPriceTextView.setText(String.format(Locale.ENGLISH, "%.2f%s", listing.getOldPrice(), priceEnding));
        holder.priceTextView.setText(String.format(Locale.ENGLISH, "%.2f%s", listing.getPrice(), priceEnding));
        holder.ratingTextView.setText(String.format(Locale.ENGLISH, "%2.1f", listing.getRating()));

        if (navController != null)
            holder.cardView.setOnClickListener(v -> navController.navigate(R.id.SellerListingDetailFragment));
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public static class ListingViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, oldPriceTextView, priceTextView, ratingTextView;
        ImageView listingCardPicture;
        CardView cardView;

        public ListingViewHolder(@NonNull View itemView) {
            super(itemView);

            listingCardPicture = itemView.findViewById(R.id.listingCardPicture);
            titleTextView = itemView.findViewById(R.id.listingCardTitle);
            oldPriceTextView = itemView.findViewById(R.id.listingCardOldPrice);
            oldPriceTextView.setPaintFlags(oldPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceTextView = itemView.findViewById(R.id.listingCardPrice);
            ratingTextView = itemView.findViewById(R.id.listingCardRating);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}