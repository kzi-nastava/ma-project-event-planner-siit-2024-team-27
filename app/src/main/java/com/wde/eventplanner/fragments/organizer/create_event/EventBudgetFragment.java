package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wde.eventplanner.databinding.FragmentEventBudgetBinding;

public class EventBudgetFragment extends Fragment {
    private FragmentEventBudgetBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventBudgetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}