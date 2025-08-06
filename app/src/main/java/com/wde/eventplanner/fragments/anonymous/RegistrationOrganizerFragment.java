package com.wde.eventplanner.fragments.anonymous;

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
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.wde.eventplanner.R;
import com.wde.eventplanner.components.CustomDropDown;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.databinding.FragmentRegistrationOrganizerBinding;
import com.wde.eventplanner.models.user.Profile;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.viewmodels.UsersViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class RegistrationOrganizerFragment extends Fragment {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private FragmentRegistrationOrganizerBinding binding;
    private UsersViewModel viewModel;
    private Uri image;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistrationOrganizerBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
                setImage(result.getData().getData());
        });

        binding.selectedImageView.setOnClickListener(v -> openImagePicker());
        binding.registerButton.setOnClickListener(v -> registerSeller());
        binding.removeImageButton.setOnClickListener(v -> removeImage());

        @SuppressWarnings("unchecked")
        CustomDropDown<String> cityDropdown = binding.inputCity;
        cityDropdown.changeValues(new ArrayList<>(Arrays.asList(binding.getRoot().getContext().getResources().getStringArray(R.array.cities))));

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

    private void registerSeller() {
        if (binding.inputName.getText() == null || binding.inputSurname.getText() == null || binding.inputEmail.getText() == null || binding.inputPassword.getText() == null ||
                binding.inputRepeatPassword.getText() == null || binding.inputPhone.getText() == null || binding.inputAddress.getText() == null) {
            SingleToast.show(requireContext(), "Error occurred, please try again!");
            return;
        }

        String name = binding.inputName.getText().toString().trim();
        String surname = binding.inputSurname.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String repeatPassword = binding.inputRepeatPassword.getText().toString().trim();
        String phone = binding.inputPhone.getText().toString().trim();
        String address = binding.inputAddress.getText().toString().trim();
        String city = (String) binding.inputCity.getSelected();

        // Input Validation
        if (name.isBlank() || surname.isBlank() || email.isBlank() || password.isBlank() || repeatPassword.isBlank() || city == null || city.isBlank()) {
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

        if (!isStrongPassword(password)) {
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

        Profile request = new Profile(email, password, true, false, "EVENTORGANIZER", name, surname, city, address, phone, null);
        viewModel.register(request).observe(getViewLifecycleOwner(), response -> {
            if (response.isBlank())
                SingleToast.show(requireContext(), "There is an existing account with the same email.");
            else {
                SingleToast.show(requireContext(), "Registration successful. A verification email has been sent.");
                if (image != null) {
                    try {
                        File imageFile = FileManager.getFileFromUri(requireContext(), image);
                        viewModel.putImage(imageFile, UUID.fromString(response)).observe(getViewLifecycleOwner(), response2 -> {
                            if (!response2.isSuccessful())
                                SingleToast.show(requireContext(), "Failed to add image to the profile!");
                            navigateToHome();
                        });
                    } catch (Exception e) {
                        SingleToast.show(requireContext(), "Failed to load the image!");
                    }
                } else navigateToHome();
            }
        });
    }

    private void navigateToHome() {
        NavController navController = Navigation.findNavController(requireView());
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(navController.getGraph().getStartDestinationId(), true)
                .build(); // Clear Backstack
        navController.navigate(R.id.action_organizer_registration_to_homepage, null, navOptions);
    }
}