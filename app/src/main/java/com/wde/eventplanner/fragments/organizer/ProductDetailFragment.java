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
import com.wde.eventplanner.databinding.FragmentOrganizerProductDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.products.Product;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.EventOrganizerViewModel;
import com.wde.eventplanner.viewmodels.ListingReviewsViewModel;
import com.wde.eventplanner.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductDetailFragment extends Fragment {
    private FragmentOrganizerProductDetailBinding binding;
    private ListingReviewsViewModel listingReviewsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrganizerProductDetailBinding.inflate(inflater, container, false);
        ProductsViewModel productsViewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);
        EventOrganizerViewModel eventOrganizerViewModel = new ViewModelProvider(requireActivity()).get(EventOrganizerViewModel.class);
        listingReviewsViewModel = new ViewModelProvider(requireActivity()).get(ListingReviewsViewModel.class);

        String staticId = requireArguments().getString("staticId");
        int version = requireArguments().getInt("version");

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        productsViewModel.getProduct(staticId).observe(getViewLifecycleOwner(), this::populateProductData);

        eventOrganizerViewModel.isListingFavourited(TokenManager.getUserId(requireContext()), ListingType.PRODUCT, staticId).observe(getViewLifecycleOwner(), isFavourite ->
                binding.favouriteButton.setIconResource(isFavourite ? R.drawable.ic_favourite_filled : R.drawable.ic_favourite)
        );

        binding.favouriteButton.setOnClickListener(v -> {
            eventOrganizerViewModel.setListingFavourite(TokenManager.getUserId(requireContext()), ListingType.PRODUCT, staticId).observe(getViewLifecycleOwner(), x ->
                    eventOrganizerViewModel.isListingFavourited(TokenManager.getUserId(requireContext()), ListingType.PRODUCT, staticId).observe(getViewLifecycleOwner(), isFavourite ->
                            binding.favouriteButton.setIconResource(isFavourite ? R.drawable.ic_favourite_filled : R.drawable.ic_favourite)
                    ));
        });

        listingReviewsViewModel.checkIfAllowed(TokenManager.getUserId(binding.getRoot().getContext()), true, UUID.fromString(staticId)).observe(getViewLifecycleOwner(),
                isAllowed -> {
                    if (isAllowed) binding.reviewButton.setVisibility(VISIBLE);
                });

        binding.reviewButton.setOnClickListener(v ->
                new ReviewDialogFragment(ListingType.PRODUCT, UUID.fromString(staticId)).show(getParentFragmentManager(), "reviewListingDialog"));

        return binding.getRoot();
    }

    private void populateProductData(Product product) {
        BuyProductChoseEventDialogFragment buyProductDialog = new BuyProductChoseEventDialogFragment(product);
        binding.buyButton.setOnClickListener(v -> buyProductDialog.show(getParentFragmentManager(), "buyProductChoseEventDialogFragment"));

        if (product.getOldPrice() != null) {
            binding.discountedPrice.setText(String.format(Locale.US, "%.2f€", product.getOldPrice()));
            binding.discountedPrice.setPaintFlags(binding.discountedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.discountedPrice.setText(null);
        }

        if (product.getRating() != null)
            binding.rating.setText(String.format(Locale.US, "%.1f", product.getRating()));
        else
            binding.rating.setText("n\\a");

        binding.price.setText(String.format(Locale.US, "%.2f€", product.getPrice()));
        binding.productTitle.setText(product.getName());
        binding.companyName.setText(product.getSellerNameAndSurname()); // todo seller page
        binding.description.setText(product.getDescription());

        ImageAdapter adapter = new ImageAdapter(getContext(), product.getImages());
        binding.viewPager.setAdapter(adapter);

        listingReviewsViewModel.getReviews(product.getStaticProductId(), true).observe(getViewLifecycleOwner(), reviews -> {
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
