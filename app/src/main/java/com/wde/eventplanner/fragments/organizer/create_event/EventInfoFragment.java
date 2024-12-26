package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.components.SingleToast;
import com.wde.eventplanner.databinding.FragmentEventInfoBinding;
import com.wde.eventplanner.models.event.GuestInfo;
import com.wde.eventplanner.models.event.GuestList;
import com.wde.eventplanner.viewmodels.CreateEventViewModel;
import com.wde.eventplanner.viewmodels.InvitationsViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EventInfoFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private FragmentEventInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventInfoBinding.inflate(inflater, container, false);
        binding.createButton.setOnClickListener(this::onCreateEventClicked);
        return binding.getRoot();
    }

    private void onCreateEventClicked(View v) {
        // u createEventViewModel će se deliti podaci između create event fragmenata
        // poziv ka serveru može da se uradi ovde
        CreateEventViewModel createEventViewModel = new ViewModelProvider(requireActivity()).get(CreateEventViewModel.class);
        InvitationsViewModel invitationsViewModel = new ViewModelProvider(requireActivity()).get(InvitationsViewModel.class);


        ArrayList<String> emails = createEventViewModel.guestList.stream().map(GuestInfo::getEmail).collect(Collectors.toCollection(ArrayList::new));
        GuestList guestList = new GuestList(emails, "eventId", "OrgName", "OrgSurname");
        invitationsViewModel.sendInvitations(guestList).observe(getViewLifecycleOwner(), error -> {
            if (error != null) SingleToast.show(requireContext(), error);
        });
    }

    @Override
    public String getTitle() {
        return "Info";
    }
}