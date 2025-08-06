package com.wde.eventplanner.fragments.admin.event_statistics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.EventStatisticsAdapter;
import com.wde.eventplanner.databinding.FragmentEventStatisticsBinding;
import com.wde.eventplanner.models.event.EventAdminDTO;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.viewmodels.EventsViewModel;

import java.util.ArrayList;

public class EventStatisticsFragment extends Fragment {
    private FragmentEventStatisticsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventStatisticsBinding.inflate(inflater, container, false);
        EventsViewModel eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);

        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.eventsRecyclerView.setNestedScrollingEnabled(false);
        binding.eventsRecyclerView.setAdapter(eventsViewModel.getPublicEvents().isInitialized() ?
                new EventStatisticsAdapter(eventsViewModel.getPublicEvents().getValue(), this) :
                new EventStatisticsAdapter(this));

        eventsViewModel.getPublicEvents().observe(getViewLifecycleOwner(), this::OnEventsChanged);
        eventsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                eventsViewModel.clearErrorMessage();
            }
        });

        eventsViewModel.fetchPublicEvents();

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void OnEventsChanged(ArrayList<EventAdminDTO> events) {
        if (binding.eventsRecyclerView.getAdapter() != null) {
            EventStatisticsAdapter adapter = (EventStatisticsAdapter) binding.eventsRecyclerView.getAdapter();
            ArrayList<EventAdminDTO> tmp = new ArrayList<>(events);
            adapter.events.clear();
            adapter.events.addAll(tmp);
            adapter.notifyDataSetChanged();
        }
    }
}
