package com.wde.eventplanner.fragments.admin.event_categories;

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

import com.wde.eventplanner.adapters.EventTypeAdapter;
import com.wde.eventplanner.databinding.FragmentEventTypesBinding;
import com.wde.eventplanner.models.EventType;
import com.wde.eventplanner.viewmodels.EventTypesViewModel;

import java.util.ArrayList;

public class EventTypesFragment extends Fragment {
    private FragmentEventTypesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventTypesBinding.inflate(inflater, container, false);
        EventTypesViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventTypesViewModel.class);

        EditEventTypeDialogFragment createDialog = new EditEventTypeDialogFragment();
        binding.createTypeButton.setOnClickListener(v -> createDialog.show(getParentFragmentManager(), "createDialog"));

        binding.typeList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.typeList.setNestedScrollingEnabled(false);
        binding.typeList.setAdapter(viewModel.getEventTypes().isInitialized() ?
                new EventTypeAdapter(viewModel.getEventTypes().getValue(), this) : new EventTypeAdapter(this));

        viewModel.getEventTypes().observe(getViewLifecycleOwner(), this::OnEventTypesChanged);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                viewModel.clearErrorMessage();
            }
        });

        viewModel.fetchEventTypes();

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void OnEventTypesChanged(ArrayList<EventType> types) {
        if (binding.typeList.getAdapter() != null) {
            EventTypeAdapter adapter = (EventTypeAdapter) binding.typeList.getAdapter();
            ArrayList<EventType> typesTmp = new ArrayList<>(types);
            adapter.types.clear();
            adapter.types.addAll(typesTmp);
            adapter.notifyDataSetChanged();
        }
        binding.typeList.setVisibility(types.isEmpty() ? View.GONE : View.VISIBLE);
    }

}
