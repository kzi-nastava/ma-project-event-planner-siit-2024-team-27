package com.wde.eventplanner.fragments.organizer.create_event;

import static com.wde.eventplanner.utils.CustomGraphicUtils.hideKeyboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCreateEventBinding;
import com.wde.eventplanner.viewmodels.CreateEventViewModel;

public class CreateEventFragment extends Fragment {
    private FragmentCreateEventBinding binding;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false);

        (new ViewModelProvider(requireActivity()).get(CreateEventViewModel.class)).clearAllData();
        binding.tabLayout.addOnTabSelectedListener(new OnTabSelectedListener());
        binding.getRoot().setOnTouchListener(this::dispatchTouchEvent);
        binding.viewPager.setOnTouchListener(this::dispatchTouchEvent);

        binding.viewPager.setAdapter(new ViewPagerAdapter<>(getParentFragmentManager(), new EventInfoFragment(),
                new EventBudgetFragment(), new EventAgendaFragment(), new EventGuestsFragment()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }

    public boolean dispatchTouchEvent(View v, MotionEvent event) {
        if (v.getId() != R.id.guestListEmailInput && v.getId() != R.id.guestListInviteButton)
            hideKeyboard(requireContext(), v);
        return false;
    }

    private class OnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            hideKeyboard(requireContext(), binding.getRoot());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    }
}
