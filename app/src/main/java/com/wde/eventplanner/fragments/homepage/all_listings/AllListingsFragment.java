package com.wde.eventplanner.fragments.homepage.all_listings;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.adapters.SortSpinnerAdapter;
import com.wde.eventplanner.fragments.homepage.ListingsViewModel;
import com.wde.eventplanner.fragments.homepage.SortSpinner;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AllListingsFragment extends Fragment {
    private final AtomicInteger selectedPosition = new AtomicInteger(0);
    private final AtomicBoolean orderDesc = new AtomicBoolean(true);
    private String selectedValue = "name";
    private RecyclerView listingsRecyclerView;
    private String searchTerms, type, category, minPrice, maxPrice, minRating, maxRating;
    private TextInputEditText searchInput;
    private ListingsViewModel listingsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_listings_screen, container, false);

        SortSpinner spinner = view.findViewById(R.id.sortSpinner);
        spinner.setAdapter(new SortSpinnerAdapter(view.getContext(), new String[]{"Name", "Price", "Rating"}, selectedPosition, orderDesc));
        spinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String value = spinner.getItemAtPosition(position).toString().toLowerCase();
                orderDesc.set(!value.equals(selectedValue) || !orderDesc.get());
                selectedPosition.set(position);
                selectedValue = value;
                refreshEvents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        listingsRecyclerView = view.findViewById(R.id.listingsRecyclerView);
        listingsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listingsRecyclerView.setNestedScrollingEnabled(false);

        ListingFilterDialogFragment filterDialog = new ListingFilterDialogFragment(this);
        Button filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> filterDialog.show(getParentFragmentManager(), "filterDialog"));

        searchInput = view.findViewById(R.id.inputSearch);
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                searchTerms = null;
                if (searchInput.getText() != null) {
                    searchTerms = searchInput.getText().toString();
                    searchTerms = !searchTerms.isBlank() ? searchTerms : null;
                }
                refreshEvents();
                return true;
            }
            return false;
        });

        listingsViewModel = new ViewModelProvider(this).get(ListingsViewModel.class);

        listingsViewModel.getListings().observe(getViewLifecycleOwner(), listings -> {
            listingsRecyclerView.setAdapter(new ListingAdapter(listings));
        });

        listingsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        return view;
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
