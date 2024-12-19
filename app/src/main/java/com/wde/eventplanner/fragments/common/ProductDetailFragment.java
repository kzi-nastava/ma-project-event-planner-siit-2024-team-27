package com.wde.eventplanner.fragments.common;

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
import com.wde.eventplanner.databinding.FragmentSellerProductDetailBinding;
import com.wde.eventplanner.databinding.FragmentUserProductDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.products.Product;
import com.wde.eventplanner.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailFragment extends Fragment {
    private FragmentUserProductDetailBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserProductDetailBinding.inflate(inflater, container, false);
        ProductsViewModel productsViewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.valueOf(0.3f, 0f, 0.5f));
        colors.add(Color.valueOf(0.5f, 0f, 1f));
        colors.add(Color.valueOf(0.7f, 0.5f, 1f));

        ImageAdapter adapter = new ImageAdapter(getContext(), colors);
        binding.viewPager.setAdapter(adapter);

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        binding.contactButton.setOnClickListener(v -> {
            // todo chat
        });

        productsViewModel.getProduct().observe(getViewLifecycleOwner(), this::populateProductData);
        // todo fixed product for now
        productsViewModel.fetchProduct("5a1b07b8-e918-4b0f-bcd2-7f1fd04dbb26");

        return binding.getRoot();
    }

    private void populateProductData(Product product) {
        if (product.getOldPrice() != null) {
            binding.discountedPrice.setText(String.format(Locale.US, "%.2f€", product.getOldPrice()));
        } else {
            binding.discountedPrice.setText(null);
        }
        if (product.getRating() != null) {
            binding.rating.setText(String.format(Locale.US, "%.1f", product.getRating()));
        } else {
            binding.rating.setText("n\\a");
        }

        binding.price.setText(String.format(Locale.US, "%.2f€", product.getPrice()));
        binding.productTitle.setText(product.getName());
        binding.companyName.setText("Company name"); // todo when seller gets his product list
        binding.description.setText(product.getDescription());

        // todo comments
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        binding.comments.setAdapter(new CommentAdapter(comments));
    }
}
