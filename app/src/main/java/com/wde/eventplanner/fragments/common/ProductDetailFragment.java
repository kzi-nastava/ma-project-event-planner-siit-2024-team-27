package com.wde.eventplanner.fragments.common;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.CommentAdapter;
import com.wde.eventplanner.adapters.ImageAdapter;
import com.wde.eventplanner.databinding.FragmentUserProductDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.chat.CreateChat;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.products.Product;
import com.wde.eventplanner.models.user.UserRole;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.ChatsViewModel;
import com.wde.eventplanner.viewmodels.ListingReviewsViewModel;
import com.wde.eventplanner.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProductDetailFragment extends Fragment {
    private FragmentUserProductDetailBinding binding;
    private ListingReviewsViewModel listingReviewsViewModel;
    private ChatsViewModel chatsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserProductDetailBinding.inflate(inflater, container, false);
        ProductsViewModel productsViewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);
        listingReviewsViewModel = new ViewModelProvider(requireActivity()).get(ListingReviewsViewModel.class);
        chatsViewModel = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);

        String staticId = requireArguments().getString("staticId");
        int version = requireArguments().getInt("version");

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        productsViewModel.getProduct(staticId).observe(getViewLifecycleOwner(), this::populateProductData);

        if (TokenManager.getRole(binding.getRoot().getContext()) == UserRole.ANONYMOUS
                || TokenManager.getRole(binding.getRoot().getContext()) == UserRole.SELLER) {
            binding.contactButton.setVisibility(GONE);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.descriptionTitle.getLayoutParams();
            params.topMargin = 0;
            binding.descriptionTitle.setLayoutParams(params);
        }

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
        binding.companyName.setText(product.getSellerNameAndSurname());
        binding.description.setText(product.getDescription());

        binding.companyName.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("sellerId", product.getSellerId().toString());
            NavHostFragment.findNavController(this).navigate(R.id.nav_seller_detail, bundle);
        });

        ImageAdapter adapter = new ImageAdapter(product.getImages());
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

        binding.contactButton.setOnClickListener(v -> {
            chatsViewModel.createChat(new CreateChat(ListingType.PRODUCT, product.getStaticProductId(), product.getVersion(), TokenManager.getProfileId(binding.getRoot().getContext()), product.getSellerProfileId()))
                    .observe(getViewLifecycleOwner(), chat -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("chatId", chat.getChatId().toString());
                        Navigation.findNavController(v).navigate(R.id.nav_chat, bundle);
                    });
        });
    }
}
