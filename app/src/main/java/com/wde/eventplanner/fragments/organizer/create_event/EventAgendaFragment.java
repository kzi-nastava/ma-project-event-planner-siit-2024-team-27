package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentEventAgendaBinding;

public class EventAgendaFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private FragmentEventAgendaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventAgendaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "Agenda";
    }
}