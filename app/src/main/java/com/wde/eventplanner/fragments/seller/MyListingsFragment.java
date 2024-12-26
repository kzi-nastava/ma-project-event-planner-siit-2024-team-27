package com.wde.eventplanner.fragments.seller;

import static com.wde.eventplanner.utils.CustomGraphicUtils.hideKeyboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.ListingAdapter;
import com.wde.eventplanner.adapters.SortSpinnerAdapter;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.databinding.FragmentMyListingsBinding;
import com.wde.eventplanner.fragments.common.homepage.all_listings.ListingFilterDialogFragment;
import com.wde.eventplanner.models.Page;
import com.wde.eventplanner.models.listing.Listing;
import com.wde.eventplanner.viewmodels.ListingsViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MyListingsFragment extends Fragment implements ListingFilterDialogFragment.ListingsFilterListener {
    private String searchTerms, type, category, minPrice, maxPrice, minRating, maxRating;
    private final AtomicInteger selectedPosition = new AtomicInteger(0);
    private final AtomicBoolean orderDesc = new AtomicBoolean(true);
    private ListingsViewModel listingsViewModel;
    private FragmentMyListingsBinding binding;
    private String selectedValue = "name";
    private Integer currentPage = 0;
    private Integer totalPages;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyListingsBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        binding.createListingButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.CreateListingFragment));

        binding.sortSpinner.setAdapter(new SortSpinnerAdapter(binding.getRoot().getContext(), new String[]{"Name", "Price", "Rating"}, selectedPosition, orderDesc));
        binding.sortSpinner.setOnItemSelectedEvenIfUnchangedListener(new SortSpinnerOnItemSelectedListener());

        binding.listingsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.listingsRecyclerView.setNestedScrollingEnabled(false);

        ListingFilterDialogFragment filterDialog = new ListingFilterDialogFragment(this);
        binding.filterButton.setOnClickListener(v -> filterDialog.show(getParentFragmentManager(), "filterDialog"));

        binding.searchInput.setOnEditorActionListener(this::onSearchInputEditorAction);
        binding.searchLayout.setEndIconOnClickListener((v) -> onSearchInputEditorAction(null, EditorInfo.IME_ACTION_SEARCH, null));

        binding.paginationPrevious.setOnClickListener(this::onClickPrevious);
        binding.paginationNext.setOnClickListener(this::onClickNext);

        listingsViewModel = viewModelProvider.get(ListingsViewModel.class);
        listingsViewModel.getListings().observe(getViewLifecycleOwner(), this::listingsObserver);
        listingsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                listingsViewModel.clearErrorMessage();
            }
        });

        binding.listingsRecyclerView.setAdapter(listingsViewModel.getListings().isInitialized() && listingsViewModel.getListings().getValue() != null ?
                new ListingAdapter(listingsViewModel.getListings().getValue().getContent(), NavHostFragment.findNavController(this)) :
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

    private void onClickPrevious(View v) {
        currentPage = Math.max(0, currentPage - 1);
        refreshEvents();
    }

    private void onClickNext(View v) {
        currentPage = Math.min(totalPages - 1, currentPage + 1);
        refreshEvents();
    }

    private boolean onSearchInputEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            searchTerms = null;
            if (binding.searchInput.getText() != null) {
                searchTerms = binding.searchInput.getText().toString();
                searchTerms = !searchTerms.isBlank() ? searchTerms : null;
            }
            hideKeyboard(requireContext(), binding.getRoot());
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
    private void listingsObserver(Page<Listing> listings) {
        if (binding.listingsRecyclerView.getAdapter() != null) {
            totalPages = listings.getTotalPages();
            binding.pageTextView.setText(String.format(Locale.ENGLISH, "%d / %d", currentPage + 1, totalPages));
            ListingAdapter adapter = (ListingAdapter) binding.listingsRecyclerView.getAdapter();
            ArrayList<Listing> listingsTmp = new ArrayList<>(listings.getContent());
            adapter.listings.clear();
            adapter.listings.addAll(listingsTmp);
            adapter.notifyDataSetChanged();
        }
    }
}
