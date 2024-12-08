package com.wde.eventplanner.fragments.common.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.NotificationsAdapter;
import com.wde.eventplanner.databinding.FragmentNotificationsBinding;
import com.wde.eventplanner.models.Notification;
import com.wde.eventplanner.viewmodels.NotificationsViewModel;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    private FragmentNotificationsBinding binding;
    private NotificationsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        binding.notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.notificationsRecyclerView.setNestedScrollingEnabled(false);

        viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
        viewModel.getNotifications().observe(getViewLifecycleOwner(), this::notificationsChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        binding.notificationsRecyclerView.setAdapter(viewModel.getNotifications().isInitialized() ?
                new NotificationsAdapter(viewModel.getNotifications().getValue()) : new NotificationsAdapter());

        viewModel.fetchNotifications();
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void notificationsChanged(ArrayList<Notification> notifications) {
        if (binding.notificationsRecyclerView.getAdapter() != null) {
            NotificationsAdapter adapter = (NotificationsAdapter) binding.notificationsRecyclerView.getAdapter();
            ArrayList<Notification> notificationsTmp = new ArrayList<>(notifications);
            adapter.notifications.clear();
            adapter.notifications.addAll(notificationsTmp);
            adapter.notifyDataSetChanged();
        }
        for (Notification notification : notifications) {
            if (!notification.isSeen())
                viewModel.readNotification(notification.getId());
        }
    }
}
