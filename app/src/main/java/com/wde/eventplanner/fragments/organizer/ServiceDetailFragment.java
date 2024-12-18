package com.wde.eventplanner.fragments.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.CommentAdapter;
import com.wde.eventplanner.adapters.ImageAdapter;
import com.wde.eventplanner.databinding.FragmentOrganizerServiceDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.services.Service;
import com.wde.eventplanner.viewmodels.ServicesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceDetailFragment extends Fragment {
    private FragmentOrganizerServiceDetailBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrganizerServiceDetailBinding.inflate(inflater, container, false);
        ServicesViewModel servicesViewModel = new ViewModelProvider(requireActivity()).get(ServicesViewModel.class);

        String staticId = requireArguments().getString("staticId");
        int version = requireArguments().getInt("version");

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        binding.buyButton.setOnClickListener(v -> {

        });

        servicesViewModel.getService().observe(getViewLifecycleOwner(), this::populateServiceData);
        servicesViewModel.fetchService(staticId);

        return binding.getRoot();
    }

    private void populateServiceData(Service service) {
        if (service.getOldPrice() != null) {
            binding.discountedPrice.setText(String.format(Locale.US, "%.2f€/hr", service.getOldPrice()));
        } else {
            binding.discountedPrice.setText(null);
        }
        if (service.getRating() != null) {
            binding.rating.setText(String.format(Locale.US, "%.1f", service.getRating()));
        } else {
            binding.rating.setText("n\\a");
        }

        binding.price.setText(String.format(Locale.US, "%.2f€/hr", service.getPrice()));
        binding.serviceTitle.setText(service.getName());
        binding.companyName.setText("Company name"); // todo when seller gets his product list
        binding.description.setText(service.getDescription());

        ImageAdapter adapter = new ImageAdapter(getContext(), service.getImages());
        binding.viewPager.setAdapter(adapter);

        // todo comments
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        binding.comments.setAdapter(new CommentAdapter(comments));
    }
}
