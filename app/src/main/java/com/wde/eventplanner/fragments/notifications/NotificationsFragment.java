package com.wde.eventplanner.fragments.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.NotificationsAdapter;
import com.wde.eventplanner.models.Notification;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications_screen, container, false);
        recyclerView = view.findViewById(R.id.notificationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NotificationsViewModel viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            recyclerView.setAdapter(new NotificationsAdapter(notifications));
            for (Notification notification : notifications) {
                if (!notification.isSeen())
                    viewModel.readNotification(notification.getId());
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.fetchNotifications();
    }
}
