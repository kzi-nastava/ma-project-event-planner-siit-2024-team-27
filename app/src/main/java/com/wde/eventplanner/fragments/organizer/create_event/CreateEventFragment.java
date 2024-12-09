package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCreateEventBinding;

public class CreateEventFragment extends Fragment {
    private FragmentCreateEventBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false);

        binding.viewPager.setAdapter(new ViewPagerAdapter<>(getParentFragmentManager(), new EventInfoFragment(),
                new EventBudgetFragment(), new EventAgendaFragment(), new EventGuestsFragment()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }

}
