package com.wde.eventplanner.fragments.organizer;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.EventAdapter;
import com.wde.eventplanner.adapters.SortSpinnerAdapter;
import com.wde.eventplanner.components.SingleToast;
import com.wde.eventplanner.databinding.FragmentMyEventsBinding;
import com.wde.eventplanner.fragments.common.homepage.all_events.EventFilterDialogFragment;
import com.wde.eventplanner.models.Page;
import com.wde.eventplanner.models.event.Event;
import com.wde.eventplanner.viewmodels.EventsViewModel;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MyEventsFragment extends Fragment implements EventFilterDialogFragment.EventsFilterListener {
    private String searchTerms, category, city, after, before, minRating, maxRating;
    private final AtomicInteger selectedPosition = new AtomicInteger(0);
    private final AtomicBoolean orderDesc = new AtomicBoolean(true);
    private FragmentMyEventsBinding binding;
    private EventsViewModel eventsViewModel;
    private String selectedValue = "name";
    private Integer currentPage = 0;
    private Integer totalPages;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        binding.createEventButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.CreateEventFragment));

        binding.sortSpinner.setAdapter(new SortSpinnerAdapter(binding.getRoot().getContext(), new String[]{"Name", "Date", "Rating"}, selectedPosition, orderDesc));
        binding.sortSpinner.setOnItemSelectedEvenIfUnchangedListener(new SortSpinnerOnItemSelectedListener());

        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.eventsRecyclerView.setNestedScrollingEnabled(false);

        EventFilterDialogFragment filterDialog = new EventFilterDialogFragment(this);
        binding.filterButton.setOnClickListener(v -> filterDialog.show(getParentFragmentManager(), "filterDialog"));

        binding.searchInput.setOnEditorActionListener(this::onSearchInputEditorAction);
        binding.searchLayout.setEndIconOnClickListener((v) -> onSearchInputEditorAction(null, EditorInfo.IME_ACTION_SEARCH, null));

        binding.paginationPrevious.setOnClickListener(this::onClickPrevious);
        binding.paginationNext.setOnClickListener(this::onClickNext);

        eventsViewModel = viewModelProvider.get(EventsViewModel.class);
        eventsViewModel.getEvents().observe(getViewLifecycleOwner(), this::eventsChanged);
        eventsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                eventsViewModel.clearErrorMessage();
            }
        });

        binding.eventsRecyclerView.setAdapter(eventsViewModel.getEvents().isInitialized() && eventsViewModel.getEvents().getValue() != null ?
                new EventAdapter(eventsViewModel.getEvents().getValue().getContent()) : new EventAdapter());

        refreshEvents();

        return binding.getRoot();
    }

    private void onClickPrevious(View v) {
        currentPage = Math.max(0, currentPage - 1);
        refreshEvents();
    }

    private void onClickNext(View v) {
        currentPage = Math.min(totalPages - 1, currentPage + 1);
        refreshEvents();
    }

    private class SortSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            String value = binding.sortSpinner.getItemAtPosition(position).toString().toLowerCase();
            orderDesc.set(!value.equals(selectedValue) || !orderDesc.get());
            selectedPosition.set(position);
            selectedValue = value;
            currentPage = 0;
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
            currentPage = 0;
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
        currentPage = 0;
        refreshEvents();
    }

    private void refreshEvents() {
        String order = orderDesc.get() ? "desc" : "asc";
        eventsViewModel.fetchEvents(searchTerms, city, category, after, before, minRating, maxRating, selectedValue, order, currentPage.toString(), "10");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void eventsChanged(Page<Event> events) {
        if (binding.eventsRecyclerView.getAdapter() != null) {
            totalPages = events.getTotalPages();
            binding.pageTextView.setText(String.format(Locale.ENGLISH, "%d / %d", currentPage + 1, totalPages));
            EventAdapter adapter = (EventAdapter) binding.eventsRecyclerView.getAdapter();
            ArrayList<Event> eventsTmp = new ArrayList<>(events.getContent());
            adapter.events.clear();
            adapter.events.addAll(eventsTmp);
            adapter.notifyDataSetChanged();
        }
    }
}
