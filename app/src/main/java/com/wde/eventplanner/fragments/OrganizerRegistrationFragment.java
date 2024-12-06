package com.wde.eventplanner.fragments;

import static com.wde.eventplanner.constants.RegexConstants.ADDRESS_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.CITY_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.EMAIL_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.NAME_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.PHONE_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.isStrongPassword;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.wde.eventplanner.databinding.FragmentOrganizerRegistrationBinding;
import com.wde.eventplanner.models.DemoUser;
import com.wde.eventplanner.R;

import java.util.ArrayList;
import java.util.List;

public class OrganizerRegistrationFragment extends Fragment {
    private FragmentOrganizerRegistrationBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image selection
    private List<DemoUser> userList;
    private Uri selectedImageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrganizerRegistrationBinding.inflate(inflater, container, false);

        userList = new ArrayList<>();
        userList.add(new DemoUser("email@email.com", "Faks1312!"));
        userList.add(new DemoUser("ftn@ftn.com", "Faks1312!"));
        userList.add(new DemoUser("a@a.com", "Faks1312!"));

        binding.selectImageButton.setOnClickListener(v -> openImagePicker());
        binding.registerButton.setOnClickListener(v -> registerSeller());

        return binding.getRoot();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            binding.selectedImageView.setImageURI(selectedImageUri);
        }
    }

    private void registerSeller() {
        String name = binding.inputName.getText().toString().trim();
        String surname = binding.inputSurname.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String repeatPassword = binding.inputRepeatPassword.getText().toString().trim();
        String phone = binding.inputPhone.getText().toString().trim();
        String city = binding.inputCity.getText().toString().trim();
        String address = binding.inputAddress.getText().toString().trim();

        // Input Validation
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || city.isEmpty()) {
            showToast("Please fill in all the required fields");
            return;
        }

        if (!name.matches(NAME_REGEX)) {
            showToast("Invalid name format");
            return;
        }

        if (!surname.matches(NAME_REGEX)) {
            showToast("Invalid surname format");
            return;
        }

        if (!email.matches(EMAIL_REGEX)) {
            showToast("Invalid email format");
            return;
        }

        if (!isValidUser(email)) {
            showToast("There is an existing account with the same email.");
            return;
        }

        if (!isStrongPassword(password)) {
            showToast("Password is too weak. Use 8+ chars, upper & lowercase, number, and special char.");
            return;
        }

        if (!password.equals(repeatPassword)) {
            showToast("Passwords do not match");
            return;
        }

        if (!phone.isEmpty() && !phone.matches(PHONE_REGEX)) {
            showToast("Invalid phone number format");
            return;
        }

        if (!city.matches(CITY_REGEX)) {
            showToast("Invalid city format");
            return;
        }

        if (!address.matches(ADDRESS_REGEX)) {
            showToast("Invalid address format");
            return;
        }

        showToast("Successful registration. Confirmation email sent.");

        NavController navController = Navigation.findNavController(requireView());
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(navController.getGraph().getStartDestinationId(), true)
                .build(); // Clear Backstack
        navController.navigate(R.id.action_organizer_registration_to_homepage, null, navOptions);
    }

    private boolean isValidUser(String email) {
        for (DemoUser user : userList) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}