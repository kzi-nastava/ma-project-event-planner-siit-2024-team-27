package com.wde.eventplanner.fragments.seller;

import static com.wde.eventplanner.constants.RegexConstants.ADDRESS_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.EMAIL_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.NAME_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.PHONE_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.isStrongPassword;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.wde.eventplanner.R;
import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.databinding.FragmentSellerProfileBinding;
import com.wde.eventplanner.models.user.Profile;
import com.wde.eventplanner.services.NotificationService;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.MenuManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.UsersViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ProfileFragment extends Fragment {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private FragmentSellerProfileBinding binding;
    private Boolean isNotificationMuted;
    private UsersViewModel viewModel;
    private Profile profile;
    private UUID profileId;
    private String city;
    private Uri image;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSellerProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);
        profileId = TokenManager.getProfileId(binding.getRoot().getContext());

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
                setImage(result.getData().getData());
        });

        binding.selectedImageView.setOnClickListener(v -> openImagePicker());
        binding.updateButton.setOnClickListener(v -> updateSeller());
        binding.removeImageButton.setOnClickListener(v -> removeImage());
        @SuppressWarnings("unchecked")
        CustomDropDown<String> cityDropdown = binding.inputCity;
        cityDropdown.changeValues(new ArrayList<>(Arrays.asList(binding.getRoot().getContext().getResources().getStringArray(R.array.cities))));

        viewModel.get(profileId).observe(getViewLifecycleOwner(), profile -> {
            isNotificationMuted = profile.getAreNotificationsMuted();
            binding.muteButton.setText(isNotificationMuted ? "Unmute notifications" : "Mute notifications");
            binding.inputName.setText(profile.getName());
            binding.inputSurname.setText(profile.getSurname());
            binding.inputEmail.setText(profile.getEmail());
            binding.inputPhone.setText(profile.getTelephoneNumber());
            binding.inputDescription.setText(profile.getDescription());
            binding.inputAddress.setText(profile.getAddress());
            binding.inputCity.setText(profile.getCity());
            city = profile.getCity();
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

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void setImage(Uri uri) {
        binding.selectedImageView.setImageURI(uri);
        binding.removeImageButton.setVisibility(View.VISIBLE);
        image = uri;
    }

    private void removeImage() {
        binding.selectedImageView.setImageResource(R.drawable.add_image);
        binding.removeImageButton.setVisibility(View.GONE);
        image = null;
    }

    private void updateSeller() {
        if (binding.inputName.getText() == null || binding.inputSurname.getText() == null || binding.inputEmail.getText() == null || binding.inputPassword.getText() == null || binding.inputRepeatPassword.getText() == null ||
                binding.inputPhone.getText() == null || binding.inputDescription.getText() == null || binding.inputAddress.getText() == null) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        String name = binding.inputName.getText().toString().trim();
        String surname = binding.inputSurname.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String repeatPassword = binding.inputRepeatPassword.getText().toString().trim();
        String phone = binding.inputPhone.getText().toString().trim();
        String description = binding.inputDescription.getText().toString().trim();
        String address = binding.inputAddress.getText().toString().trim();
        if (city == null)
            city = (String) binding.inputCity.getSelected();

        // Input Validation
        if (name.isBlank() || surname.isBlank() || email.isBlank() || city == null || city.isBlank()) {
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

        if (!phone.isEmpty() && !phone.matches(PHONE_REGEX)) {
            SingleToast.show(requireContext(), "Invalid phone number format");
            return;
        }

        if (!address.matches(ADDRESS_REGEX)) {
            SingleToast.show(requireContext(), "Invalid address format");
            return;
        }

        if (!description.isEmpty() && description.length() < 10) {
            SingleToast.show(requireContext(), "Description is too short. Please provide at least 10 characters.");
            return;
        }

        Profile request = new Profile(email, password, null, isNotificationMuted, null, name, surname, city, address, phone, description);
        updateProfile(request);
    }

    private void updateProfile(Profile profile) {
        viewModel.update(profileId, profile).observe(getViewLifecycleOwner(), response -> {
            if (response == null)
                SingleToast.show(requireContext(), "There was a problem with updating your data.");
            else {
                SingleToast.show(requireContext(), "Update successful.");
                this.profile = profile;
                if (image != null) {
                    try {
                        File imageFile = FileManager.getFileFromUri(requireContext(), image);
                        viewModel.putImage(imageFile, profileId).observe(getViewLifecycleOwner(), response2 -> {
                            if (!response2.isSuccessful())
                                SingleToast.show(requireContext(), "Failed to add image to the profile!");
                        });
                    } catch (Exception e) {
                        SingleToast.show(requireContext(), "Failed to load the image!");
                    }
                }
            }
        });
    }
}
