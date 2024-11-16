package com.wde.eventplanner.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.CommentAdapter;
import com.wde.eventplanner.adapters.ImageAdapter;
import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.Listing;

import java.util.ArrayList;
import java.util.List;

public class SellerListingDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_detail_screen, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.valueOf(0.3f, 0f, 0.5f));
        colors.add(Color.valueOf(0.5f, 0f, 1f));
        colors.add(Color.valueOf(0.7f, 0.5f, 1f));

        ImageAdapter adapter = new ImageAdapter(getContext(), colors);
        viewPager.setAdapter(adapter);

        RecyclerView commentsRecyclerView = view.findViewById(R.id.comments);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        commentsRecyclerView.setNestedScrollingEnabled(false);

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        comments.add(new Comment("John Smith", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        commentsRecyclerView.setAdapter(new CommentAdapter(comments));

        Button editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.SellerCreateServiceFragment);
        });

        return view;
    }
}
