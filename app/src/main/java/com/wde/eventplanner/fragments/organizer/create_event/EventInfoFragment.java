package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentEventInfoBinding;
import com.wde.eventplanner.viewmodels.CreateEventViewModel;

public class EventInfoFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private FragmentEventInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventInfoBinding.inflate(inflater, container, false);
        binding.createButton.setOnClickListener(this::onCreateClicked);
        return binding.getRoot();
    }

    private void onCreateClicked(View v) {
        CreateEventViewModel createEventViewModel = new ViewModelProvider(requireActivity()).get(CreateEventViewModel.class);
        // u createEventViewModel će se deliti podaci između create event fragmenata
        // poziv ka serveru može da se uradi ovde
    }

    @Override
    public String getTitle() {
        return "Info";
    }
}