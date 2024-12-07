package com.wde.eventplanner.fragments.common.homepage.all_listings;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.adapters.SortSpinnerAdapter;
import com.wde.eventplanner.databinding.FragmentAllListingsBinding;
import com.wde.eventplanner.viewmodels.ListingsViewModel;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AllListingsFragment extends Fragment {
    private final AtomicInteger selectedPosition = new AtomicInteger(0);
    private final AtomicBoolean orderDesc = new AtomicBoolean(true);
    private String selectedValue = "name";
    private String searchTerms, type, category, minPrice, maxPrice, minRating, maxRating;
    private ListingsViewModel listingsViewModel;
    private FragmentAllListingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllListingsBinding.inflate(inflater, container, false);

        binding.sortSpinner.setAdapter(new SortSpinnerAdapter(binding.getRoot().getContext(), new String[]{"Name", "Price", "Rating"}, selectedPosition, orderDesc));
        binding.sortSpinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
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
        });

        binding.listingsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.listingsRecyclerView.setNestedScrollingEnabled(false);

        ListingFilterDialogFragment filterDialog = new ListingFilterDialogFragment(this);
        binding.filterButton.setOnClickListener(v -> filterDialog.show(getParentFragmentManager(), "filterDialog"));

        binding.searchInput.setOnEditorActionListener((v, actionId, event) -> {
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
        });

        listingsViewModel = new ViewModelProvider(this).get(ListingsViewModel.class);

        listingsViewModel.getListings().observe(getViewLifecycleOwner(), listings -> {
            binding.listingsRecyclerView.setAdapter(new ListingAdapter(listings));
        });

        listingsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshEvents();
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

    public void refreshEvents() {
        String order = orderDesc.get() ? "desc" : "asc";
        listingsViewModel.fetchListings(searchTerms, type, category, minPrice, maxPrice, minRating, maxRating, selectedValue, order, "0", "10");
    }
}
