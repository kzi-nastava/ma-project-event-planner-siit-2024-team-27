package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentEventGuestsBinding;

public class EventGuestsFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private FragmentEventGuestsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventGuestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "Guests";
    }
}