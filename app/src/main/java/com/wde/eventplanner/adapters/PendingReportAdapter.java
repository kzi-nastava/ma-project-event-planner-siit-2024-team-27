package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardPendingReportBinding;
import com.wde.eventplanner.models.reports.PendingStatus;
import com.wde.eventplanner.models.reports.UserReport;
import com.wde.eventplanner.models.reports.UserReportResponse;
import com.wde.eventplanner.viewmodels.UserReportsViewModel;

import java.util.ArrayList;
import java.util.List;

public class PendingReportAdapter extends RecyclerView.Adapter<PendingReportAdapter.PendingReportsAdapterHolder> {
    public final List<UserReportResponse> reports;
    private final UserReportsViewModel viewModel;

    public PendingReportAdapter(UserReportsViewModel viewModel) {
        this.reports = new ArrayList<>();
        this.viewModel = viewModel;
    }

    public PendingReportAdapter(List<UserReportResponse> reports, UserReportsViewModel viewModel) {
        this.viewModel = viewModel;
        this.reports = reports;
    }

    @NonNull
    @Override
    public PendingReportsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardPendingReportBinding binding = CardPendingReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PendingReportsAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingReportsAdapterHolder holder, int position) {
        UserReportResponse report = reports.get(position);
        holder.binding.authorTextView.setText(report.getProfileToCredentials());
        holder.binding.reasonTextView.setText(report.getReason());
        holder.binding.approveButton.setOnClickListener(v -> viewModel.processReport(
                new UserReport(report.getId(), null, null, null, PendingStatus.APPROVED, null, null)));
        holder.binding.declineButton.setOnClickListener(v -> viewModel.processReport(
                new UserReport(report.getId(), null, null, null, PendingStatus.DECLINED, null, null)));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public static class PendingReportsAdapterHolder extends RecyclerView.ViewHolder {
        CardPendingReportBinding binding;

        public PendingReportsAdapterHolder(CardPendingReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
