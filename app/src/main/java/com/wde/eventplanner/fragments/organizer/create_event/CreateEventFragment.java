package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCreateEventBinding;

public class CreateEventFragment extends Fragment {
    private FragmentCreateEventBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false);

        binding.viewPager.setAdapter(new ViewPagerAdapter(requireActivity(), new EventInfoFragment(),
                new EventBudgetFragment(), new EventAgendaFragment(), new EventGuestsFragment()));

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Info");
            else if (position == 1) tab.setText("Budget");
            else if (position == 2) tab.setText("Agenda");
            else if (position == 3) tab.setText("Guests");
        }).attach();

        return binding.getRoot();
    }

}
