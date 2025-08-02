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
import com.wde.eventplanner.databinding.FragmentSellerProductDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.products.Product;
import com.wde.eventplanner.viewmodels.ListingReviewsViewModel;
import com.wde.eventplanner.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProductDetailFragment extends Fragment {
    private FragmentSellerProductDetailBinding binding;
    private ListingReviewsViewModel listingReviewsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSellerProductDetailBinding.inflate(inflater, container, false);
        ProductsViewModel productsViewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);
        listingReviewsViewModel = new ViewModelProvider(requireActivity()).get(ListingReviewsViewModel.class);

        String staticId = requireArguments().getString("staticId");
        int version = requireArguments().getInt("version");

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        binding.editButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.EditProductFragment, requireArguments());
        });

        binding.deleteButton.setOnClickListener(v -> {
            productsViewModel.deleteProduct(staticId).observe(getViewLifecycleOwner(),
                    x -> requireActivity().getSupportFragmentManager().popBackStack());
        });

        productsViewModel.getProduct(staticId).observe(getViewLifecycleOwner(), this::populateProductData);

        return binding.getRoot();
    }

    private void populateProductData(Product product) {
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
