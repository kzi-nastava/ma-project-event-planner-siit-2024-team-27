package com.wde.eventplanner.fragments.common;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wde.eventplanner.utils.MapsUtil.getLocationFromAddress;
import static com.wde.eventplanner.utils.MapsUtil.joinCity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.CommentAdapter;
import com.wde.eventplanner.adapters.ImageAdapter;
import com.wde.eventplanner.databinding.FragmentUserEventDetailBinding;
import com.wde.eventplanner.models.Comment;
import com.wde.eventplanner.models.event.EventDetailedDTO;
import com.wde.eventplanner.models.user.UserRole;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.EventReviewsViewModel;
import com.wde.eventplanner.viewmodels.EventsViewModel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class EventDetailFragment extends Fragment implements OnMapReadyCallback {
    private EventReviewsViewModel eventReviewsViewModel;
    private FragmentUserEventDetailBinding binding;
    private EventsViewModel eventsViewModel;
    private GoogleMap mMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserEventDetailBinding.inflate(inflater, container, false);
        eventReviewsViewModel = new ViewModelProvider(requireActivity()).get(EventReviewsViewModel.class);
        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
        else Log.e("EventDetailFragment", "Map fragment is null!");

        fetchEvent();

        return binding.getRoot();
    }

    private void fetchEvent() {
        String id = requireArguments().getString("id");
        boolean isGuest = TokenManager.getRole(requireContext()) == UserRole.GUEST;
        UUID userId = TokenManager.getUserId(requireContext());
        userId = userId != null ? userId : UUID.randomUUID();
        eventsViewModel.getEvent(UUID.fromString(id), isGuest, userId)
                .observe(getViewLifecycleOwner(), this::populateServiceData);
    }

    private void populateServiceData(EventDetailedDTO event) {
        binding.pdfButton.setOnClickListener(v -> eventsViewModel.downloadReport(event.getId(), event.getName(), binding.getRoot().getContext())
                .observe(getViewLifecycleOwner(), file -> FileManager.openPdf(requireContext(), file)));

        binding.eventTitle.setText(event.getName());
        binding.organizerName.setText(event.getOrganizerCredentials());

        binding.eventCardTime.setText(event.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        binding.eventCardDate.setText(event.getDate().format(DateTimeFormatter.ofPattern("d.M.yyyy.")));
        binding.eventCardLocation.setText(joinCity(event.getAddress(), event.getCity()));
        binding.eventCardRating.setText(String.format(Locale.ENGLISH, "%2.1f", event.getAverageRating()));

        binding.description.setText(event.getDescription());

        ImageAdapter adapter = new ImageAdapter(event.getImages());
        binding.viewPager.setAdapter(adapter);

        eventReviewsViewModel.getReviews(event.getId()).observe(getViewLifecycleOwner(), reviews -> {
            ArrayList<Comment> comments = reviews.stream().map(review ->
                    new Comment(review.getGuestName() + " " + review.getGuestSurname(), review.getComment())).collect(Collectors.toCollection(ArrayList::new));
            binding.comments.setAdapter(new CommentAdapter(comments));

            if (!comments.isEmpty()) {
                binding.noCommentsTitle.setVisibility(GONE);
                binding.comments.setVisibility(VISIBLE);
            }
        });

        LatLng location = getLocationFromAddress(event.getAddress(), event.getCity(), requireContext());
        if (location != null) {
            mMap.addMarker(new MarkerOptions().position(location).title(event.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
}
