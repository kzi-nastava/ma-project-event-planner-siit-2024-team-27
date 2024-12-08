package com.wde.eventplanner.fragments.seller;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.CommentAdapter;
import com.wde.eventplanner.adapters.ImageAdapter;
import com.wde.eventplanner.databinding.FragmentSellerListingDetailBinding;
import com.wde.eventplanner.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class ListingDetailFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSellerListingDetailBinding binding = FragmentSellerListingDetailBinding.inflate(inflater, container, false);

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.valueOf(0.3f, 0f, 0.5f));
        colors.add(Color.valueOf(0.5f, 0f, 1f));
        colors.add(Color.valueOf(0.7f, 0.5f, 1f));

        ImageAdapter adapter = new ImageAdapter(getContext(), colors);
        binding.viewPager.setAdapter(adapter);

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        binding.comments.setAdapter(new CommentAdapter(comments));

        binding.editButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.SellerCreateServiceFragment);
        });

        return binding.getRoot();
    }
}
