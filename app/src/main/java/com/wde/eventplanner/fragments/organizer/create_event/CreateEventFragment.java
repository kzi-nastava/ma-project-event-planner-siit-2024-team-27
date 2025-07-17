package com.wde.eventplanner.fragments.organizer.create_event;

import static com.wde.eventplanner.utils.CustomGraphicUtils.hideKeyboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.wde.eventplanner.R;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCreateEventBinding;
import com.wde.eventplanner.models.event.ListingBudgetItemDTO;
import com.wde.eventplanner.models.productBudgetItem.ProductBudgetItemDTO;
import com.wde.eventplanner.models.serviceBudgetItem.ServiceBudgetItem;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.CreateEventViewModel;
import com.wde.eventplanner.viewmodels.EventsViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CreateEventFragment extends Fragment {
    private FragmentCreateEventBinding binding;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false);

        try {
            CreateEventViewModel createEventViewModel = new ViewModelProvider(requireActivity()).get(CreateEventViewModel.class);
            createEventViewModel.clearAllData();

            EventsViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);
            String eventId = requireArguments().getString("id");
            String organizerId = TokenManager.getUserId(requireContext()).toString();

            viewModel.fetchEventFromOrganizer(organizerId, eventId).observe(getViewLifecycleOwner(), (eventComplexView -> {
                createEventViewModel.event = eventComplexView;
                createEventViewModel.budgetItems.addAll(
                        eventComplexView.getProductBudgetItems().stream().map(ProductBudgetItemDTO::toListingBudgetItem).collect(Collectors.toList())
                );
                createEventViewModel.budgetItems.addAll(
                        eventComplexView.getServiceBudgetItems().stream().map(ServiceBudgetItem::toListingBudgetItem).collect(Collectors.toList())
                );
                createEventViewModel.originalBudgetItems = createEventViewModel.budgetItems.stream().map(ListingBudgetItemDTO::new).collect(Collectors.toCollection(ArrayList::new));
                viewModel.fetchAgenda(eventId).observe(getViewLifecycleOwner(), (agendaItems ->
                        createEventViewModel.agendaItems = agendaItems));
            }));

        } catch (Exception ignored) {
        }

        binding.tabLayout.addOnTabSelectedListener(new OnTabSelectedListener());
        binding.getRoot().setOnTouchListener(this::dispatchTouchEvent);
        binding.viewPager.setOnTouchListener(this::dispatchTouchEvent);

        binding.viewPager.setAdapter(new ViewPagerAdapter<>(getParentFragmentManager(), new EventInfoFragment(),
                new EventBudgetFragment(), new EventAgendaFragment(), new EventGuestsFragment()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }

    public boolean dispatchTouchEvent(View v, MotionEvent event) {
        if (v.getId() != R.id.guestListEmailInput && v.getId() != R.id.guestListInviteButton)
            hideKeyboard(requireContext(), v);
        return false;
    }

    private class OnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            hideKeyboard(requireContext(), binding.getRoot());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    }
}
