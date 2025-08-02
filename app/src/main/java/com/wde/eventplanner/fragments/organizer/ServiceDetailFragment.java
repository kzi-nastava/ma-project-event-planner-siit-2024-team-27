package com.wde.eventplanner.fragments.organizer;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.CommentAdapter;
import com.wde.eventplanner.adapters.ImageAdapter;
import com.wde.eventplanner.databinding.FragmentOrganizerServiceDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.services.Service;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.EventOrganizerViewModel;
import com.wde.eventplanner.viewmodels.ListingReviewsViewModel;
import com.wde.eventplanner.viewmodels.ServicesViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServiceDetailFragment extends Fragment {
    private FragmentOrganizerServiceDetailBinding binding;
    private ListingReviewsViewModel listingReviewsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrganizerServiceDetailBinding.inflate(inflater, container, false);
        ServicesViewModel servicesViewModel = new ViewModelProvider(requireActivity()).get(ServicesViewModel.class);
        EventOrganizerViewModel eventOrganizerViewModel = new ViewModelProvider(requireActivity()).get(EventOrganizerViewModel.class);
        listingReviewsViewModel = new ViewModelProvider(requireActivity()).get(ListingReviewsViewModel.class);

        String staticId = requireArguments().getString("staticId");
        int version = requireArguments().getInt("version");

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        servicesViewModel.getService(staticId).observe(getViewLifecycleOwner(), this::populateServiceData);

        eventOrganizerViewModel.isListingFavourited(TokenManager.getUserId(requireContext()), ListingType.SERVICE, staticId).observe(getViewLifecycleOwner(), isFavourite ->
                binding.favouriteButton.setIconResource(isFavourite ? R.drawable.ic_favourite_filled : R.drawable.ic_favourite)
        );

        binding.favouriteButton.setOnClickListener(v -> {
            eventOrganizerViewModel.setListingFavourite(TokenManager.getUserId(requireContext()), ListingType.SERVICE, staticId).observe(getViewLifecycleOwner(), x ->
                    eventOrganizerViewModel.isListingFavourited(TokenManager.getUserId(requireContext()), ListingType.SERVICE, staticId).observe(getViewLifecycleOwner(), isFavourite ->
                            binding.favouriteButton.setIconResource(isFavourite ? R.drawable.ic_favourite_filled : R.drawable.ic_favourite)
                    ));
        });

        listingReviewsViewModel.checkIfAllowed(TokenManager.getUserId(binding.getRoot().getContext()), false, UUID.fromString(staticId)).observe(getViewLifecycleOwner(),
                isAllowed -> {
                    if (isAllowed) binding.reviewButton.setVisibility(VISIBLE);
                });

        binding.reviewButton.setOnClickListener(v ->
                new ReviewDialogFragment(ListingType.SERVICE, UUID.fromString(staticId)).show(getParentFragmentManager(), "reviewListingDialog"));

        return binding.getRoot();
    }

    private void populateServiceData(Service service) {
        ReserveServiceDialog reserveServiceDialog = new ReserveServiceDialog(service);
        binding.buyButton.setOnClickListener(v -> reserveServiceDialog.show(getParentFragmentManager(), "reserveServiceDialog"));

        if (service.getOldPrice() != null) {
            binding.discountedPrice.setText(String.format(Locale.US, "%.2f€/hr", service.getOldPrice()));
            binding.discountedPrice.setPaintFlags(binding.discountedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else
            binding.discountedPrice.setText(null);

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
