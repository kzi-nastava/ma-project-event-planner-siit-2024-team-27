package com.wde.eventplanner.fragments.common;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.databinding.DialogReportBinding;
import com.wde.eventplanner.models.reports.UserReport;
import com.wde.eventplanner.models.user.UserBlock;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.UserReportsViewModel;
import com.wde.eventplanner.viewmodels.UsersViewModel;

import java.util.UUID;

public class ReportDialog extends DialogFragment {
    private final UUID reportProfileId;
    private final boolean hideBlockButton;

    public ReportDialog(UUID reportProfileId, boolean hideBlockButton) {
        this.reportProfileId = reportProfileId;
        this.hideBlockButton = hideBlockButton;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogReportBinding binding = DialogReportBinding.inflate(inflater, container, false);
        UserReportsViewModel userReportsViewModel = new ViewModelProvider(requireActivity()).get(UserReportsViewModel.class);
        UsersViewModel usersViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);

        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.reportButton.setOnClickListener(v -> {
            if (binding.comment.getText() == null || binding.comment.getText().toString().isBlank()) {
                SingleToast.show(binding.getRoot().getContext(), "Reporting requires a reason!");
                return;
            }
            String reason = binding.comment.getText().toString();

            userReportsViewModel.createReport(new UserReport(null, reason, null, null, null, reportProfileId, TokenManager.getProfileId(binding.getRoot().getContext()))).observe(getViewLifecycleOwner(), done -> {
                SingleToast.show(binding.getRoot().getContext(), "Report created successfully!");
                dismiss();
            });
        });

        if (hideBlockButton)
            binding.blockButton.setVisibility(GONE);

        binding.blockButton.setOnClickListener(v -> {
            usersViewModel.block(new UserBlock(TokenManager.getProfileId(binding.getRoot().getContext()), reportProfileId)).observe(getViewLifecycleOwner(), done -> {
                SingleToast.show(binding.getRoot().getContext(), "User blocked successfully!");
                dismiss();
            });
        });

        return binding.getRoot();
    }
}
