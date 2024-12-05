package com.wde.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.fragments.homepage.all_events.EventFilterDialogFragment;
import com.wde.eventplanner.models.Listing;

import java.util.ArrayList;
import java.util.List;

public class SellerMyListingsFragment extends Fragment {

    private MaterialButton createServiceButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_my_listings_screen, container, false);

        createServiceButton = view.findViewById(R.id.createServiceButton);
        createServiceButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            // Navigate to RegistrationFragment
            navController.navigate(R.id.SellerCreateServiceFragment);
        });

        Spinner spinner = view.findViewById(R.id.sortSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"Price", "Rating", "Name"});
        spinner.setAdapter(adapter);

        RecyclerView listingsRecyclerView = view.findViewById(R.id.listingsRecyclerView);
        listingsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listingsRecyclerView.setNestedScrollingEnabled(false);

        List<Listing> listingsList = new ArrayList<>();
//        listingsList.add(new Listing("Best product ever", "200€", "199.99€", 4.5f));
//        listingsList.add(new Listing("Best service ever", "60€/hr", "59.99€/hr", 4.5f));
//        listingsList.add(new Listing("Best product ever", "200€", "199.99€", 4.5f));
//        listingsList.add(new Listing("Best service ever", "60€/hr", "59.99€/hr", 4.5f));
//        listingsList.add(new Listing("Best product ever", "200€", "199.99€", 4.5f));

        ListingAdapter listingAdapter = new ListingAdapter(listingsList, NavHostFragment.findNavController(this));
        listingsRecyclerView.setAdapter(listingAdapter);

//        EventFilterDialogFragment filterDialog = new EventFilterDialogFragment();
//        Button filterButton = view.findViewById(R.id.filterButton);
//        filterButton.setOnClickListener(v -> filterDialog.show(getParentFragmentManager(), "filterDialog"));

        return view;
    }
}
