package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardPendingReviewBinding;
import com.wde.eventplanner.fragments.admin.reviews.ReviewsFragment;
import com.wde.eventplanner.models.reviews.ListingReviewResponse;
import com.wde.eventplanner.models.reviews.ReviewHandling;
import com.wde.eventplanner.utils.MenuManager;
import com.wde.eventplanner.viewmodels.ListingReviewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class PendingListingReviewAdapter extends RecyclerView.Adapter<PendingListingReviewAdapter.PendingReviewAdapterHolder> {
    public final List<ListingReviewResponse> reviews;
    private final NavController navController;
    private ListingReviewsViewModel viewModel;
    private final ReviewsFragment parent;

    public PendingListingReviewAdapter(ReviewsFragment parent, NavController navController) {
        this.navController = navController;
        this.reviews = new ArrayList<>();
        this.parent = parent;
    }

    public PendingListingReviewAdapter(List<ListingReviewResponse> reviews, ReviewsFragment parent, NavController navController) {
        this.navController = navController;
        this.reviews = reviews;
        this.parent = parent;
    }

    @NonNull
    @Override
    public PendingReviewAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardPendingReviewBinding binding = CardPendingReviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        viewModel = new ViewModelProvider(this.parent.requireActivity()).get(ListingReviewsViewModel.class);
        return new PendingReviewAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingReviewAdapterHolder holder, int position) {
        ListingReviewResponse review = reviews.get(position);
        holder.binding.authorTextView.setText(String.format("%s %s", review.getGuestName(), review.getGuestSurname()));
        holder.binding.reviewTextView.setText(review.getComment());

        holder.binding.detailButton.setOnClickListener(v ->
                MenuManager.navigateToFragment(review.getListingType().toString(), review.getListingId().toString(), parent.requireContext(), navController));
        holder.binding.approveButton.setOnClickListener(v -> viewModel.processReview(new ReviewHandling(review.getId(), true)));
        holder.binding.declineButton.setOnClickListener(v -> viewModel.processReview(new ReviewHandling(review.getId(), false)));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class PendingReviewAdapterHolder extends RecyclerView.ViewHolder {
        CardPendingReviewBinding binding;

        public PendingReviewAdapterHolder(CardPendingReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
