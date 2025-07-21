package com.wde.eventplanner.fragments.seller.calendar;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarUtils;
import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.CalendarServiceAdapter;
import com.wde.eventplanner.databinding.FragmentCalendarServicesBinding;
import com.wde.eventplanner.models.event.CalendarEvent;
import com.wde.eventplanner.models.event.CalendarService;
import com.wde.eventplanner.models.listing.Listing;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.SellerViewModel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CalendarFragment extends Fragment {
    private FragmentCalendarServicesBinding binding;
    private ArrayList<CalendarEvent> calendar;
    private SellerViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarServicesBinding.inflate(inflater, container, false);
        Drawable dot = CalendarUtils.getDrawableText(binding.getRoot().getContext(), "◉", Typeface.DEFAULT, R.color.secondary, 16); //•

        binding.servicesRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.servicesRecyclerView.setNestedScrollingEnabled(false);
        binding.calendarView.setOnCalendarDayClickListener(this::showServicesList);

        viewModel = new ViewModelProvider(requireActivity()).get(SellerViewModel.class);
        viewModel.getCalendar(TokenManager.getUserId(requireContext())).observe(getViewLifecycleOwner(), calendar -> {
            ArrayList<CalendarDay> calendarDays = new ArrayList<>();
            for (CalendarEvent event : calendar) {
                LocalDate d = event.getDate();
                Calendar c = Calendar.getInstance();
                c.set(d.getYear(), d.getMonthValue() - 1, d.getDayOfMonth());
                CalendarDay calendarDay = new CalendarDay(c);
                calendarDay.setImageDrawable(dot);
                calendarDays.add(calendarDay);
            }
            binding.calendarView.setCalendarDays(calendarDays);
            this.calendar = calendar;
            showServicesList(new CalendarDay(Calendar.getInstance()));
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                viewModel.clearErrorMessage();
            }
        });

        return binding.getRoot();
    }

    private void showServicesList(CalendarDay calendarDay) {
        Calendar day = calendarDay.getCalendar();
        ArrayList<CalendarEvent> events = calendar.stream().filter(event -> event.getDate().isEqual(Instant.ofEpochMilli(day.getTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate())).collect(Collectors.toCollection(ArrayList::new));

        HashMap<String, CalendarService> serviceMap = new HashMap<>();

        for (CalendarEvent event : events) {
            for (Listing listing : event.getListings()) {
                if (listing.getType() == ListingType.SERVICE)
                    serviceMap.computeIfAbsent(listing.getId() + "-" + listing.getVersion(), k ->
                            new CalendarService(listing.getId(), listing.getVersion(), listing.getName(), new ArrayList<>())).getEvents().add(event);
            }
        }

        binding.servicesRecyclerView.setAdapter(new CalendarServiceAdapter(new ArrayList<>(serviceMap.values()), NavHostFragment.findNavController(this), getParentFragmentManager()));
    }
}
