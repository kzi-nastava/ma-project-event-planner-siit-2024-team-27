package com.wde.eventplanner.fragments.guest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.databinding.DialogReviewBinding;
import com.wde.eventplanner.models.event.EventReview;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.EventReviewsViewModel;

import java.util.UUID;

public class ReviewDialogFragment extends DialogFragment {
    private EventReviewsViewModel viewModel;
    private DialogReviewBinding binding;
    private final UUID eventId;

    public ReviewDialogFragment(UUID eventId) {
        this.eventId = eventId;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogReviewBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(EventReviewsViewModel.class);

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

        viewModel.createReview(new EventReview(grade, comment, eventId, TokenManager.getUserId(binding.getRoot().getContext())))
                .observe(getViewLifecycleOwner(), done -> {
                    SingleToast.show(requireContext(), done);
                    dismiss();
                });
    }
}
