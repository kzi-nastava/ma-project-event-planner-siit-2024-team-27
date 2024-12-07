package com.wde.eventplanner.fragments.seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.databinding.FragmentMyListingsBinding;
import com.wde.eventplanner.models.listing.Listing;

import java.util.ArrayList;
import java.util.List;

public class MyListingsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMyListingsBinding binding = FragmentMyListingsBinding.inflate(inflater, container, false);

        binding.createServiceButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            // Navigate to RegistrationFragment
            navController.navigate(R.id.SellerCreateServiceFragment);
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"Price", "Rating", "Name"});
        binding.sortSpinner.setAdapter(adapter);

        binding.listingsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.listingsRecyclerView.setNestedScrollingEnabled(false);

        List<Listing> listingsList = new ArrayList<>();
//        listingsList.add(new Listing("Best product ever", "200€", "199.99€", 4.5f));
//        listingsList.add(new Listing("Best service ever", "60€/hr", "59.99€/hr", 4.5f));
//        listingsList.add(new Listing("Best product ever", "200€", "199.99€", 4.5f));
//        listingsList.add(new Listing("Best service ever", "60€/hr", "59.99€/hr", 4.5f));
//        listingsList.add(new Listing("Best product ever", "200€", "199.99€", 4.5f));

        ListingAdapter listingAdapter = new ListingAdapter(listingsList, NavHostFragment.findNavController(this));
        binding.listingsRecyclerView.setAdapter(listingAdapter);

//        EventFilterDialogFragment filterDialog = new EventFilterDialogFragment();
//        binding.filterButton.setOnClickListener(v -> filterDialog.show(getParentFragmentManager(), "filterDialog"));

        return binding.getRoot();
    }
}
