package com.wde.eventplanner.fragments.common.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.NotificationsAdapter;
import com.wde.eventplanner.databinding.FragmentNotificationsBinding;
import com.wde.eventplanner.models.Notification;
import com.wde.eventplanner.viewmodels.NotificationsViewModel;

public class NotificationsFragment extends Fragment {
    private FragmentNotificationsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        binding.notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.notificationsRecyclerView.setNestedScrollingEnabled(false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NotificationsViewModel viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            binding.notificationsRecyclerView.setAdapter(new NotificationsAdapter(notifications));
            for (Notification notification : notifications) {
                if (!notification.isSeen())
                    viewModel.readNotification(notification.getId());
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        viewModel.fetchNotifications();
    }
}
