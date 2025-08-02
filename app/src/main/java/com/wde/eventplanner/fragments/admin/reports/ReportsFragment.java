package com.wde.eventplanner.fragments.admin.reports;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.PendingReportAdapter;
import com.wde.eventplanner.databinding.FragmentReportsBinding;
import com.wde.eventplanner.models.reports.UserReportResponse;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.viewmodels.UserReportsViewModel;

import java.util.ArrayList;

public class ReportsFragment extends Fragment {
    private FragmentReportsBinding binding;

    @Override
    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(inflater, container, false);
        UserReportsViewModel userReportsViewModel = new ViewModelProvider(requireActivity()).get(UserReportsViewModel.class);

        binding.reports.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.reports.setNestedScrollingEnabled(false);
        binding.reports.setAdapter(userReportsViewModel.getReports().isInitialized() ?
                new PendingReportAdapter(userReportsViewModel.getReports().getValue(), userReportsViewModel) : new PendingReportAdapter(userReportsViewModel));

        userReportsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                userReportsViewModel.clearErrorMessage();
            }
        });

        userReportsViewModel.getReports().observe(getViewLifecycleOwner(), reports -> {
            if (binding.reports.getAdapter() != null) {
                PendingReportAdapter adapter = (PendingReportAdapter) binding.reports.getAdapter();
                ArrayList<UserReportResponse> r = new ArrayList<>(reports);
                adapter.reports.clear();
                adapter.reports.addAll(r);
                adapter.notifyDataSetChanged();
            }
        });

        userReportsViewModel.fetchReports();

        return binding.getRoot();
    }
}
