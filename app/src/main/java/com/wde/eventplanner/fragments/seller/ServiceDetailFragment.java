package com.wde.eventplanner.fragments.seller;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.CommentAdapter;
import com.wde.eventplanner.adapters.ImageAdapter;
import com.wde.eventplanner.databinding.FragmentSellerServiceDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.services.Service;
import com.wde.eventplanner.viewmodels.ServicesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceDetailFragment extends Fragment {
    private FragmentSellerServiceDetailBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSellerServiceDetailBinding.inflate(inflater, container, false);
        ServicesViewModel servicesViewModel = new ViewModelProvider(requireActivity()).get(ServicesViewModel.class);

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.valueOf(0.3f, 0f, 0.5f));
        colors.add(Color.valueOf(0.5f, 0f, 1f));
        colors.add(Color.valueOf(0.7f, 0.5f, 1f));

        ImageAdapter adapter = new ImageAdapter(getContext(), colors);
        binding.viewPager.setAdapter(adapter);

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        binding.editButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.CreateListingFragment);
        });

        servicesViewModel.getService().observe(getViewLifecycleOwner(), this::populateServiceData);
        // todo fixed service for now
        servicesViewModel.fetchService("0792d0dd-044d-43df-8031-5f9377522502");

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

        // todo comments
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        binding.comments.setAdapter(new CommentAdapter(comments));
    }
}
