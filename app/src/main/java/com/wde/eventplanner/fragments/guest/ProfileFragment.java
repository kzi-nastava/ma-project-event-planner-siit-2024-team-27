package com.wde.eventplanner.fragments.guest;

import static com.wde.eventplanner.constants.RegexConstants.EMAIL_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.NAME_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.isStrongPassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.wde.eventplanner.databinding.FragmentGuestProfileBinding;
import com.wde.eventplanner.models.user.Profile;
import com.wde.eventplanner.services.NotificationService;
import com.wde.eventplanner.utils.MenuManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.UsersViewModel;

import java.util.UUID;

public class ProfileFragment extends Fragment {
    private FragmentGuestProfileBinding binding;
    private Boolean isNotificationMuted;
    private UsersViewModel viewModel;
    private Profile profile;
    private UUID profileId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGuestProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);
        profileId = TokenManager.getProfileId(binding.getRoot().getContext());

        binding.updateButton.setOnClickListener(v -> updateGuest());

        viewModel.get(profileId).observe(getViewLifecycleOwner(), profile -> {
            isNotificationMuted = profile.getAreNotificationsMuted();
            binding.muteButton.setText(isNotificationMuted ? "Unmute notifications" : "Mute notifications");
            binding.inputName.setText(profile.getName());
            binding.inputSurname.setText(profile.getSurname());
            binding.inputEmail.setText(profile.getEmail());
            this.profile = profile;
        });

        binding.muteButton.setOnClickListener(v -> {
            isNotificationMuted = !isNotificationMuted;
            binding.muteButton.setText(isNotificationMuted ? "Unmute notifications" : "Mute notifications");
            profile.setAreNotificationsMuted(isNotificationMuted);
            updateProfile(profile);
        });

        binding.deleteButton.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Are you sure you want to delete your profile?")
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        viewModel.delete(profileId);
                        TokenManager.clearToken(binding.getRoot().getContext());
                        MenuManager.adjustMenu(requireActivity());
                        NotificationService.unsubscribe();
                    })
                    .setNegativeButton("Cancel", null)
                    .create().show();
        });

        return binding.getRoot();
    }

    private void updateGuest() {
        if (binding.inputName.getText() == null || binding.inputSurname.getText() == null || binding.inputEmail.getText() == null ||
                binding.inputPassword.getText() == null || binding.inputRepeatPassword.getText() == null) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        String name = binding.inputName.getText().toString().trim();
        String surname = binding.inputSurname.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String repeatPassword = binding.inputRepeatPassword.getText().toString().trim();

        // Input Validation
        if (name.isBlank() || surname.isBlank() || email.isBlank()) {
            SingleToast.show(requireContext(), "Please fill in all the required fields");
            return;
        }

        if (!name.matches(NAME_REGEX)) {
            SingleToast.show(requireContext(), "Invalid name format");
            return;
        }

        if (!surname.matches(NAME_REGEX)) {
            SingleToast.show(requireContext(), "Invalid surname format");
            return;
        }

        if (!email.matches(EMAIL_REGEX)) {
            SingleToast.show(requireContext(), "Invalid email format");
            return;
        }

        if (!password.isBlank() && !isStrongPassword(password)) {
            SingleToast.show(requireContext(), "Password is too weak. Use 8+ chars, upper & lowercase, number, and special char.");
            return;
        }

        if (!password.equals(repeatPassword)) {
            SingleToast.show(requireContext(), "Passwords do not match");
            return;
        }

        Profile request = new Profile(email, password, null, isNotificationMuted, null, name, surname, null, null, null, null);
        updateProfile(request);
    }

    private void updateProfile(Profile profile) {
        viewModel.update(profileId, profile).observe(getViewLifecycleOwner(), response -> {
            if (response == null)
                SingleToast.show(requireContext(), "There was a problem with updating your data.");
            else {
                SingleToast.show(requireContext(), "Update successful.");
                this.profile = profile;
            }
        });
    }
}