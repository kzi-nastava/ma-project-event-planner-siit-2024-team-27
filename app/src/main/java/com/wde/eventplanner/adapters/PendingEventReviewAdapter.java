package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardPendingReviewBinding;
import com.wde.eventplanner.fragments.admin.reviews.ReviewsFragment;
import com.wde.eventplanner.models.reviews.EventReviewResponse;
import com.wde.eventplanner.models.reviews.ReviewHandling;
import com.wde.eventplanner.viewmodels.EventReviewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class PendingEventReviewAdapter extends RecyclerView.Adapter<PendingEventReviewAdapter.PendingReviewAdapterHolder> {
    public final List<EventReviewResponse> reviews;
    private final NavController navController;
    private EventReviewsViewModel viewModel;
    private final ReviewsFragment parent;

    public PendingEventReviewAdapter(ReviewsFragment parent, NavController navController) {
        this.navController = navController;
        this.reviews = new ArrayList<>();
        this.parent = parent;
    }

    public PendingEventReviewAdapter(List<EventReviewResponse> reviews, ReviewsFragment parent, NavController navController) {
        this.navController = navController;
        this.reviews = reviews;
        this.parent = parent;
    }

    @NonNull
    @Override
    public PendingReviewAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardPendingReviewBinding binding = CardPendingReviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        viewModel = new ViewModelProvider(this.parent.requireActivity()).get(EventReviewsViewModel.class);
        return new PendingReviewAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingReviewAdapterHolder holder, int position) {
        EventReviewResponse review = reviews.get(position);
        holder.binding.authorTextView.setText(String.format("%s %s", review.getGuestName(), review.getGuestSurname()));
        holder.binding.reviewTextView.setText(review.getComment());

//         todo event detailed view
//         Bundle bundle = new Bundle();
//         bundle.putString("eventId", review.getEventId().toString());
//         holder.binding.detailButton.setOnClickListener(v -> navController.navigate(fragmentId, bundle));
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
