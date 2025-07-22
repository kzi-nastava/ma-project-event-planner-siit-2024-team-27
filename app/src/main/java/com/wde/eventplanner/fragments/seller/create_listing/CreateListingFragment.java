package com.wde.eventplanner.fragments.seller.create_listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCreateListingBinding;

public class CreateListingFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentCreateListingBinding binding = FragmentCreateListingBinding.inflate(inflater, container, false);

        binding.viewPager.setAdapter(new ViewPagerAdapter<>(getParentFragmentManager(), new CreateProductFragment(), new CreateServiceFragment()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }
}
