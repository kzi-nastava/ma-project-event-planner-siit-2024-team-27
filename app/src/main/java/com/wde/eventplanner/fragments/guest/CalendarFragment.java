package com.wde.eventplanner.fragments.guest;

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
import com.wde.eventplanner.adapters.CalendarEventAdapter;
import com.wde.eventplanner.databinding.FragmentCalendarEventsBinding;
import com.wde.eventplanner.models.event.CalendarEvent;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.GuestsViewModel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;

public class CalendarFragment extends Fragment {
    private FragmentCalendarEventsBinding binding;
    private ArrayList<CalendarEvent> calendar;
    private GuestsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarEventsBinding.inflate(inflater, container, false);
        Drawable dot = CalendarUtils.getDrawableText(binding.getRoot().getContext(), "◉", Typeface.DEFAULT, R.color.secondary, 16); //•

        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.eventsRecyclerView.setNestedScrollingEnabled(false);
        binding.calendarView.setOnCalendarDayClickListener(this::showEventList);

        viewModel = new ViewModelProvider(requireActivity()).get(GuestsViewModel.class);
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
            showEventList(new CalendarDay(Calendar.getInstance()));
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                viewModel.clearErrorMessage();
            }
        });

        return binding.getRoot();
    }

    private void showEventList(CalendarDay calendarDay) {
        Calendar day = calendarDay.getCalendar();
        ArrayList<CalendarEvent> events = calendar.stream().filter(event -> event.getDate().isEqual(Instant.ofEpochMilli(day.getTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate())).collect(Collectors.toCollection(ArrayList::new));
        binding.eventsRecyclerView.setAdapter(new CalendarEventAdapter(events, NavHostFragment.findNavController(this), getParentFragmentManager()));
    }
}
