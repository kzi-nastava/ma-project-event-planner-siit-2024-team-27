package com.wde.eventplanner.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.databinding.DialogReviewBinding;
import com.wde.eventplanner.models.listing.ListingReview;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.ListingReviewsViewModel;

import java.util.UUID;

public class ReviewDialogFragment extends DialogFragment {
    private ListingReviewsViewModel viewModel;
    private final ListingType listingType;
    private DialogReviewBinding binding;
    private final UUID listingId;

    public ReviewDialogFragment(ListingType listingType, UUID listingId) {
        this.listingType = listingType;
        this.listingId = listingId;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogReviewBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ListingReviewsViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.reviewButton.setOnClickListener(v -> createReview());

        return binding.getRoot();
    }

    private void createReview() {
        if (binding.grade.getText() == null || binding.comment.getText() == null) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        String comment = binding.comment.getText().toString().trim();
        String gradeStr = binding.grade.getText().toString().trim();

        // Input Validation
        if (comment.isBlank() || gradeStr.isBlank()) {
            SingleToast.show(requireContext(), "Please fill in all the required fields");
            return;
        }

        int grade;
        try {
            grade = Integer.parseInt(gradeStr);
        } catch (Exception e) {
            SingleToast.show(requireContext(), "Please fill in all the required fields");
            return;
        }

        if (grade < 1 || grade > 5) {
            SingleToast.show(requireContext(), "Grade must be between 1 and 5!");
            return;
        }

        viewModel.createReview(new ListingReview(grade, comment, listingType, listingId, TokenManager.getUserId(binding.getRoot().getContext())))
                .observe(getViewLifecycleOwner(), done -> {
                    SingleToast.show(requireContext(), done);
                    dismiss();
                });
    }
}
