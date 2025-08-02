package com.wde.eventplanner.fragments.organizer;

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
import androidx.navigation.fragment.NavHostFragment;
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
import com.wde.eventplanner.databinding.FragmentOrganizerEventDetailBinding;
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
    private FragmentOrganizerEventDetailBinding binding;
    private EventsViewModel eventsViewModel;
    private GoogleMap mMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrganizerEventDetailBinding.inflate(inflater, container, false);
        eventReviewsViewModel = new ViewModelProvider(requireActivity()).get(EventReviewsViewModel.class);
        eventsViewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
        String id = requireArguments().getString("id");

        binding.comments.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.comments.setNestedScrollingEnabled(false);

        boolean isGuest = TokenManager.getRole(requireContext()) == UserRole.GUEST;
        eventsViewModel.getEvent(UUID.fromString(id), isGuest, TokenManager.getUserId(requireContext()))
                .observe(getViewLifecycleOwner(), this::populateEventData);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
        else Log.e("EventDetailFragment", "Map fragment is null!");

        return binding.getRoot();
    }

    private void populateEventData(EventDetailedDTO event) {
        binding.pdfButton.setOnClickListener(v -> eventsViewModel.downloadReport(event.getId(), event.getName())
                .observe(getViewLifecycleOwner(), file -> FileManager.openPdf(requireContext(), file)));

        binding.deleteButton.setOnClickListener(v -> {
            eventsViewModel.deleteEvent(event.getId(), TokenManager.getUserId(binding.getRoot().getContext()));
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.editButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.CreateEventFragment, requireArguments()));

        binding.eventTitle.setText(event.getName());
        binding.organizerName.setText(event.getOrganizerCredentials());

        binding.eventCardTime.setText(event.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        binding.eventCardDate.setText(event.getDate().format(DateTimeFormatter.ofPattern("d.M.yyyy.")));
        binding.eventCardLocation.setText(joinCity(event.getAddress(), event.getCity()));
        binding.eventCardRating.setText(String.format(Locale.ENGLISH, "%2.1f", event.getAverageRating()));

        binding.description.setText(event.getDescription());

        ImageAdapter adapter = new ImageAdapter(getContext(), event.getImages());
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
