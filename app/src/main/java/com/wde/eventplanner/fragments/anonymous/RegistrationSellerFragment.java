package com.wde.eventplanner.fragments.anonymous;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.wde.eventplanner.R;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.databinding.FragmentRegistrationSellerBinding;
import com.wde.eventplanner.models.DemoUser;

import java.util.ArrayList;
import java.util.List;

public class RegistrationSellerFragment extends Fragment {
    private FragmentRegistrationSellerBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image selection
    private Uri selectedImageUri;
    private List<DemoUser> userList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistrationSellerBinding.inflate(inflater, container, false);

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
        String companyName = binding.inputCompanyName.getText().toString().trim();
        String phone = binding.inputPhone.getText().toString().trim();
        String description = binding.inputDescription.getText().toString().trim();
        String city = binding.inputCity.getText().toString().trim();
        String address = binding.inputAddress.getText().toString().trim();

        // Input Validation
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || companyName.isEmpty() || city.isEmpty()) {
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

        if (!isValidUser(email)) {
            SingleToast.show(requireContext(), "There is an existing account with the same email.");
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

        if (!city.matches(CITY_REGEX)) {
            SingleToast.show(requireContext(), "Invalid city format");
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

        SingleToast.show(requireContext(), "Successful registration. Confirmation email sent.");

        NavController navController = Navigation.findNavController(requireView());
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(navController.getGraph().getStartDestinationId(), true)
                .build(); // Clear Backstack
        navController.navigate(R.id.action_seller_registration_to_homepage, null, navOptions);

    }

    private boolean isValidUser(String email) {
        for (DemoUser user : userList) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }
}
