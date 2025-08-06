package com.wde.eventplanner.fragments.common;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.adapters.CommentAdapter;
import com.wde.eventplanner.databinding.FragmentSellerDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.user.UserRole;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.SellerViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class SellerDetailFragment extends Fragment {
    private FragmentSellerDetailBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSellerDetailBinding.inflate(inflater, container, false);
        SellerViewModel sellerViewModel = new ViewModelProvider(requireActivity()).get(SellerViewModel.class);

        String sellerId = requireArguments().getString("sellerId");

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        sellerViewModel.getSeller(UUID.fromString(sellerId)).observe(getViewLifecycleOwner(), seller -> {
            binding.sellerTitle.setText(String.format("%s %s", seller.getName(), seller.getSurname()));
            binding.description.setText(seller.getDescription());
            Picasso.get().load(seller.getImage()).into(binding.image);
            binding.email.setText(seller.getEmail());
            binding.phone.setText(seller.getTelephoneNumber());
            binding.rating.setText(String.format(Locale.US, "%.1f", seller.getRating()));
            binding.location.setText(String.format("%s, %s", seller.getAddress(), seller.getCity()));

            ArrayList<Comment> comments = seller.getReviews().stream().map(review ->
                    new Comment(review.getGuestName() + " " + review.getGuestSurname(), review.getComment())).collect(Collectors.toCollection(ArrayList::new));
            binding.comments.setAdapter(new CommentAdapter(comments));

            if (!comments.isEmpty()) {
                binding.noCommentsTitle.setVisibility(GONE);
                binding.comments.setVisibility(VISIBLE);
            }

            binding.reportButton.setOnClickListener(v ->
                    new ReportDialog(seller.getSellerProfileId(), true).show(getParentFragmentManager(), "reportDialog"));

            if (TokenManager.getRole(binding.getRoot().getContext()) == UserRole.ANONYMOUS)
                binding.reportButton.setVisibility(INVISIBLE);
        });

        return binding.getRoot();
    }
}
