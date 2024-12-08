package com.wde.eventplanner.fragments.seller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.adapters.SortSpinnerAdapter;
import com.wde.eventplanner.databinding.FragmentMyListingsBinding;
import com.wde.eventplanner.fragments.common.homepage.all_listings.ListingFilterDialogFragment;
import com.wde.eventplanner.models.listing.Listing;
import com.wde.eventplanner.viewmodels.ListingsViewModel;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MyListingsFragment extends Fragment implements ListingFilterDialogFragment.ListingsFilterListener {
    private String searchTerms, type, category, minPrice, maxPrice, minRating, maxRating;
    private final AtomicInteger selectedPosition = new AtomicInteger(0);
    private final AtomicBoolean orderDesc = new AtomicBoolean(true);
    private ListingsViewModel listingsViewModel;
    private FragmentMyListingsBinding binding;
    private String selectedValue = "name";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyListingsBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        binding.createServiceButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.SellerCreateServiceFragment);
        });

        binding.sortSpinner.setAdapter(new SortSpinnerAdapter(binding.getRoot().getContext(), new String[]{"Name", "Price", "Rating"}, selectedPosition, orderDesc));
        binding.sortSpinner.setOnItemSelectedEvenIfUnchangedListener(new SortSpinnerOnItemSelectedListener());

        binding.listingsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.listingsRecyclerView.setNestedScrollingEnabled(false);

        ListingFilterDialogFragment filterDialog = new ListingFilterDialogFragment(this);
        binding.filterButton.setOnClickListener(v -> filterDialog.show(getParentFragmentManager(), "filterDialog"));

        binding.searchInput.setOnEditorActionListener(this::onSearchInputEditorAction);

        listingsViewModel = viewModelProvider.get(ListingsViewModel.class);
        listingsViewModel.getListings().observe(getViewLifecycleOwner(), this::listingsObserver);
        listingsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        binding.listingsRecyclerView.setAdapter(listingsViewModel.getListings().isInitialized() ?
                new ListingAdapter(listingsViewModel.getListings().getValue(), NavHostFragment.findNavController(this)) :
                new ListingAdapter(NavHostFragment.findNavController(this)));

        refreshEvents();

        return binding.getRoot();
    }

    private class SortSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            String value = binding.sortSpinner.getItemAtPosition(position).toString().toLowerCase();
            orderDesc.set(!value.equals(selectedValue) || !orderDesc.get());
            selectedPosition.set(position);
            selectedValue = value;
            refreshEvents();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
        }
    }

    private boolean onSearchInputEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            searchTerms = null;
            if (binding.searchInput.getText() != null) {
                searchTerms = binding.searchInput.getText().toString();
                searchTerms = !searchTerms.isBlank() ? searchTerms : null;
            }
            refreshEvents();
            return true;
        }
        return false;
    }

    public void onFilterPressed(String type, String category, String minPrice, String maxPrice, String minRating, String maxRating) {
        this.type = type;
        this.category = category;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minRating = minRating;
        this.maxRating = maxRating;
        refreshEvents();
    }

    private void refreshEvents() {
        String order = orderDesc.get() ? "desc" : "asc";
        listingsViewModel.fetchListings(searchTerms, type, category, minPrice, maxPrice, minRating, maxRating, selectedValue, order, "0", "10");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void listingsObserver(ArrayList<Listing> listings) {
        if (binding.listingsRecyclerView.getAdapter() != null) {
            ListingAdapter adapter = (ListingAdapter) binding.listingsRecyclerView.getAdapter();
            ArrayList<Listing> listingsTmp = new ArrayList<>(listings);
            adapter.listings.clear();
            adapter.listings.addAll(listingsTmp);
            adapter.notifyDataSetChanged();
        }
    }
}
