package com.wde.eventplanner.fragments.seller.catalogue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCatalogueBinding;

public class CatalogueFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentCatalogueBinding binding = FragmentCatalogueBinding.inflate(inflater, container, false);

        binding.viewPager.setAdapter(new ViewPagerAdapter<>(getParentFragmentManager(), new ProductCatalogueFragment(), new ServiceCatalogueFragment()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }
}
