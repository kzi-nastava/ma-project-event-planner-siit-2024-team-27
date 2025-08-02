package com.wde.eventplanner.fragments.seller;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.graphics.Paint;
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
import com.wde.eventplanner.viewmodels.ListingReviewsViewModel;
import com.wde.eventplanner.viewmodels.ServicesViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class ServiceDetailFragment extends Fragment {
    private FragmentSellerServiceDetailBinding binding;
    private ListingReviewsViewModel listingReviewsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSellerServiceDetailBinding.inflate(inflater, container, false);
        ServicesViewModel servicesViewModel = new ViewModelProvider(requireActivity()).get(ServicesViewModel.class);
        listingReviewsViewModel = new ViewModelProvider(requireActivity()).get(ListingReviewsViewModel.class);

        String staticId = requireArguments().getString("staticId");
        int version = requireArguments().getInt("version");

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        binding.editButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.EditServiceFragment, requireArguments());
        });

        binding.deleteButton.setOnClickListener(v -> {
            servicesViewModel.deleteService(staticId).observe(getViewLifecycleOwner(),
                    x -> requireActivity().getSupportFragmentManager().popBackStack());
        });

        servicesViewModel.getService(staticId).observe(getViewLifecycleOwner(), this::populateServiceData);

        return binding.getRoot();
    }

    private void populateServiceData(Service service) {
        if (service.getOldPrice() != null) {
            binding.discountedPrice.setText(String.format(Locale.US, "%.2f€/hr", service.getOldPrice()));
            binding.discountedPrice.setPaintFlags(binding.discountedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.discountedPrice.setText(null);
        }

        if (service.getRating() != null)
            binding.rating.setText(String.format(Locale.US, "%.1f", service.getRating()));
        else
            binding.rating.setText("n\\a");

        binding.price.setText(String.format(Locale.US, "%.2f€/hr", service.getPrice()));
        binding.serviceTitle.setText(service.getName());
        binding.companyName.setText(service.getSellerNameAndSurname()); // todo seller page
        binding.description.setText(service.getDescription());

        ImageAdapter adapter = new ImageAdapter(getContext(), service.getImages());
        binding.viewPager.setAdapter(adapter);

        listingReviewsViewModel.getReviews(service.getStaticServiceId(), false).observe(getViewLifecycleOwner(), reviews -> {
            ArrayList<Comment> comments = reviews.stream().map(review ->
                    new Comment(review.getGuestName() + " " + review.getGuestSurname(), review.getComment())).collect(Collectors.toCollection(ArrayList::new));
            binding.comments.setAdapter(new CommentAdapter(comments));

            if (!comments.isEmpty()) {
                binding.noCommentsTitle.setVisibility(GONE);
                binding.comments.setVisibility(VISIBLE);
            }
        });
    }
}
