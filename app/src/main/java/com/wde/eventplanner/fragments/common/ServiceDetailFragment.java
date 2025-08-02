package com.wde.eventplanner.fragments.common;

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
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.CommentAdapter;
import com.wde.eventplanner.adapters.ImageAdapter;
import com.wde.eventplanner.databinding.FragmentUserServiceDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.chat.CreateChat;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.services.Service;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.ChatsViewModel;
import com.wde.eventplanner.viewmodels.ListingReviewsViewModel;
import com.wde.eventplanner.viewmodels.ServicesViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class ServiceDetailFragment extends Fragment {
    private FragmentUserServiceDetailBinding binding;
    private ListingReviewsViewModel listingReviewsViewModel;
    private ChatsViewModel chatsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserServiceDetailBinding.inflate(inflater, container, false);
        ServicesViewModel servicesViewModel = new ViewModelProvider(requireActivity()).get(ServicesViewModel.class);
        listingReviewsViewModel = new ViewModelProvider(requireActivity()).get(ListingReviewsViewModel.class);
        chatsViewModel = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);

        String staticId = requireArguments().getString("staticId");
        int version = requireArguments().getInt("version");

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

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
        binding.companyName.setText(service.getSellerNameAndSurname());
        binding.description.setText(service.getDescription());

        binding.companyName.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("sellerId", service.getSellerId().toString());
            NavHostFragment.findNavController(this).navigate(R.id.nav_seller_detail, bundle);
        });

        ImageAdapter adapter = new ImageAdapter(service.getImages());
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

        binding.contactButton.setOnClickListener(v -> {
            chatsViewModel.createChat(new CreateChat(ListingType.SERVICE, service.getStaticServiceId(), service.getVersion(), TokenManager.getProfileId(binding.getRoot().getContext()), service.getSellerProfileId()))
                    .observe(getViewLifecycleOwner(), chat -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("chatId", chat.getChatId().toString());
                        Navigation.findNavController(v).navigate(R.id.nav_chat, bundle);
                    });
        });
    }
}
