package com.wde.eventplanner.fragments.homepage.all_events;

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
import com.wde.eventplanner.adapters.EventAdapter;
import com.wde.eventplanner.adapters.SortSpinnerAdapter;
import com.wde.eventplanner.fragments.homepage.EventsViewModel;
import com.wde.eventplanner.fragments.homepage.SortSpinner;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class AllEventsFragment extends Fragment {
    private final AtomicInteger selectedPosition = new AtomicInteger(0);
    private AtomicBoolean orderDesc = new AtomicBoolean(true);
    private String selectedValue = "name";
    private RecyclerView eventsRecyclerView;
    private String searchTerms, category, city, after, before, minRating, maxRating;
    private TextInputEditText searchInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_events_screen, container, false);

        SortSpinner spinner = view.findViewById(R.id.sortSpinner);
        spinner.setAdapter(new SortSpinnerAdapter(view.getContext(), new String[]{"Name", "Date", "Rating"}, selectedPosition, orderDesc));
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

        eventsRecyclerView = view.findViewById(R.id.listingsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        eventsRecyclerView.setNestedScrollingEnabled(false);

        EventFilterDialogFragment filterDialog = new EventFilterDialogFragment(this);
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

        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshEvents();
    }

    public void onFilterPressed(String category, String city, Date after, Date before, Double minRating, Double maxRating) {
        this.category = category;
        this.city = city;
        this.after = after != null ? after.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT) : null;
        this.before = before != null ? before.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT) : null;
        this.minRating = minRating != null ? minRating.toString() : null;
        this.maxRating = maxRating != null ? maxRating.toString() : null;
        refreshEvents();
    }

    public void refreshEvents() {
        EventsViewModel eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);

        eventsViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            eventsRecyclerView.setAdapter(new EventAdapter(events));
        });

        eventsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        String order = orderDesc.get() ? "desc" : "asc";

        eventsViewModel.fetchEvents(searchTerms, city, category, after, before, minRating, maxRating, selectedValue, order, "0", "10");
    }
}
