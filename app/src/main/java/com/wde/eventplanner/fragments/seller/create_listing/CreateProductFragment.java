package com.wde.eventplanner.fragments.seller.create_listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCreateProductBinding;

public class CreateProductFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private FragmentCreateProductBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "Product";
    }
}