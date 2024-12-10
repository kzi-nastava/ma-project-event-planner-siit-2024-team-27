package com.wde.eventplanner.fragments.common.homepage.all_events;

import static com.wde.eventplanner.components.CustomGraphicUtils.hideKeyboard;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.EventAdapter;
import com.wde.eventplanner.adapters.SortSpinnerAdapter;
import com.wde.eventplanner.databinding.FragmentAllEventsBinding;
import com.wde.eventplanner.models.Event;
import com.wde.eventplanner.viewmodels.EventsViewModel;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AllEventsFragment extends Fragment implements EventFilterDialogFragment.EventsFilterListener {
    private String searchTerms, category, city, after, before, minRating, maxRating;
    private final AtomicInteger selectedPosition = new AtomicInteger(0);
    private final AtomicBoolean orderDesc = new AtomicBoolean(true);
    private FragmentAllEventsBinding binding;
    private EventsViewModel eventsViewModel;
    private String selectedValue = "name";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllEventsBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        binding.sortSpinner.setAdapter(new SortSpinnerAdapter(binding.getRoot().getContext(), new String[]{"Name", "Date", "Rating"}, selectedPosition, orderDesc));
        binding.sortSpinner.setOnItemSelectedEvenIfUnchangedListener(new SortSpinnerOnItemSelectedListener());

        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.eventsRecyclerView.setNestedScrollingEnabled(false);

        EventFilterDialogFragment filterDialog = new EventFilterDialogFragment(this);
        binding.filterButton.setOnClickListener(v -> filterDialog.show(getParentFragmentManager(), "filterDialog"));

        binding.searchInput.setOnEditorActionListener(this::onSearchInputEditorAction);
        binding.searchLayout.setEndIconOnClickListener((v) -> onSearchInputEditorAction(null, EditorInfo.IME_ACTION_SEARCH, null));

        eventsViewModel = viewModelProvider.get(EventsViewModel.class);
        eventsViewModel.getEvents().observe(getViewLifecycleOwner(), this::eventsChanged);
        eventsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                eventsViewModel.clearErrorMessage();
            }
        });

        binding.eventsRecyclerView.setAdapter(eventsViewModel.getEvents().isInitialized() ?
                new EventAdapter(eventsViewModel.getEvents().getValue()) : new EventAdapter());

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
            hideKeyboard(requireContext(), binding.getRoot());
            refreshEvents();
            return true;
        }
        return false;
    }

    public void onFilterPressed(String category, String city, Date after, Date before, String minRating, String maxRating) {
        this.category = category;
        this.city = city;
        this.after = after != null ? after.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT) : null;
        this.before = before != null ? before.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT) : null;
        this.minRating = minRating;
        this.maxRating = maxRating;
        refreshEvents();
    }

    private void refreshEvents() {
        String order = orderDesc.get() ? "desc" : "asc";
        eventsViewModel.fetchEvents(searchTerms, city, category, after, before, minRating, maxRating, selectedValue, order, "0", "10");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void eventsChanged(ArrayList<Event> events) {
        if (binding.eventsRecyclerView.getAdapter() != null) {
            EventAdapter adapter = (EventAdapter) binding.eventsRecyclerView.getAdapter();
            ArrayList<Event> eventsTmp = new ArrayList<>(events);
            adapter.events.clear();
            adapter.events.addAll(eventsTmp);
            adapter.notifyDataSetChanged();
        }
    }
}
