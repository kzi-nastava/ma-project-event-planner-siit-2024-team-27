package com.wde.eventplanner.fragments.organizer.create_event;

import static com.wde.eventplanner.components.CustomGraphicUtils.hideKeyboard;
import static com.wde.eventplanner.constants.RegexConstants.EMAIL_REGEX;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.GuestListAdapter;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.components.ItemDividerDecoration;
import com.wde.eventplanner.databinding.FragmentEventGuestsBinding;
import com.wde.eventplanner.models.GuestInfo;

import java.util.ArrayList;

public class EventGuestsFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private FragmentEventGuestsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventGuestsBinding.inflate(inflater, container, false);
        binding.emailInput.setOnEditorActionListener(this::onEmailInputEditorAction);
        binding.inviteButton.setOnClickListener((v) -> sendInvite());
        binding.pdfButton.setOnClickListener((v) -> downloadPdf());

        ArrayList<GuestInfo> guests = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            guests.add(new GuestInfo("John " + i, "Smith", "john.smith@gmail.com"));

        binding.guestList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.guestList.addItemDecoration(new ItemDividerDecoration(getContext(), getResources()));
        binding.guestList.setAdapter(new GuestListAdapter(guests));
        return binding.getRoot();
    }

    private boolean onEmailInputEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            hideKeyboard(requireContext(), binding.getRoot());
            sendInvite();
            return true;
        }
        return false;
    }

    private void sendInvite() {
        if (binding.emailInput.getText() != null) {
            String email = binding.emailInput.getText().toString().toLowerCase().trim();
            if (!email.matches(EMAIL_REGEX))
                Toast.makeText(getActivity(), "Invalid email address!", Toast.LENGTH_SHORT).show();
            else {
                // send invite
            }
        }
    }

    private void downloadPdf() {
        // download pdf
    }

    @Override
    public String getTitle() {
        return "Guests";
    }
}