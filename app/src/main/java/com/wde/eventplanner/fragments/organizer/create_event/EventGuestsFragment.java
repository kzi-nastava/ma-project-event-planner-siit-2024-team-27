package com.wde.eventplanner.fragments.organizer.create_event;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.GuestListAdapter;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.components.ItemDividerDecoration;
import com.wde.eventplanner.databinding.FragmentEventGuestsBinding;
import com.wde.eventplanner.models.event.GuestInfo;
import com.wde.eventplanner.viewmodels.CreateEventViewModel;
import com.wde.eventplanner.viewmodels.InvitationsViewModel;

public class EventGuestsFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private FragmentEventGuestsBinding binding;
    private CreateEventViewModel createEventViewModel;
    private InvitationsViewModel invitationsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventGuestsBinding.inflate(inflater, container, false);
        createEventViewModel = new ViewModelProvider(requireActivity()).get(CreateEventViewModel.class);
        invitationsViewModel = new ViewModelProvider(requireActivity()).get(InvitationsViewModel.class);

        binding.guestListEmailInput.setOnEditorActionListener(this::onEmailInputEditorAction);
        binding.guestListInviteButton.setOnClickListener((v) -> addGuest());
        binding.pdfButton.setOnClickListener((v) -> downloadPdf());

        binding.guestList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.guestList.addItemDecoration(new ItemDividerDecoration(getContext(), getResources()));
        binding.guestList.setAdapter(new GuestListAdapter(createEventViewModel.guestList));
        if (createEventViewModel.guestList.isEmpty())
            binding.guestList.setVisibility(View.GONE);

        return binding.getRoot();
    }

    private boolean onEmailInputEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            addGuest();
            return true;
        }
        return false;
    }

    private void addGuest() {
        if (binding.guestListEmailInput.getText() != null) {
            String email = binding.guestListEmailInput.getText().toString().toLowerCase().trim();
            if (!email.matches(EMAIL_REGEX))
                Toast.makeText(getActivity(), "Invalid email address!", Toast.LENGTH_SHORT).show();
            else if (createEventViewModel.guestList.stream().noneMatch(guestInfo -> guestInfo.getEmail().equals(email))) {
                createEventViewModel.guestList.add(new GuestInfo(null, null, email));
                int position = createEventViewModel.guestList.size() - 1;
                binding.guestList.setVisibility(View.VISIBLE);
                binding.guestListEmailInput.setText("");

                if (binding.guestList.getAdapter() != null)
                    binding.guestList.getAdapter().notifyItemChanged(position);

                invitationsViewModel.fetchGuestInfo(email).observe(getViewLifecycleOwner(), guestInfo -> {
                    String name, surname = null;
                    if (guestInfo != null) {
                        name = guestInfo.getName();
                        surname = guestInfo.getSurname();
                    } else
                        name = "Anonymous";

                    createEventViewModel.guestList.get(position).setName(name);
                    createEventViewModel.guestList.get(position).setSurname(surname);

                    if (binding.guestList.getAdapter() != null)
                        binding.guestList.getAdapter().notifyItemChanged(position);
                });
            } else
                Toast.makeText(getActivity(), "Guest is already invited!", Toast.LENGTH_SHORT).show();
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