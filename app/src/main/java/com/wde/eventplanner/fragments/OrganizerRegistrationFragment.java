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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.DemoUser;
import com.wde.eventplanner.R;

import java.util.ArrayList;
import java.util.List;

public class OrganizerRegistrationFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image selection
    private TextInputEditText inputName, inputSurname, inputEmail, inputPassword, inputRepeatPassword;
    private TextInputEditText  inputPhone, inputCity, inputAddress;
    private MaterialButton selectImageButton, registerButton;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private List<DemoUser> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_registration_screen, container, false); // Inflate the layout

        userList = new ArrayList<>();
        userList.add(new DemoUser("email@email.com", "Faks1312!"));
        userList.add(new DemoUser("ftn@ftn.com", "Faks1312!"));
        userList.add(new DemoUser("a@a.com", "Faks1312!"));

        // Initialize views
        inputName = view.findViewById(R.id.inputName);
        inputSurname = view.findViewById(R.id.inputSurname);
        inputEmail = view.findViewById(R.id.inputEmail);
        inputPassword = view.findViewById(R.id.inputPassword);
        inputRepeatPassword = view.findViewById(R.id.inputRepeatPassword);
        inputPhone = view.findViewById(R.id.inputPhone);
        inputCity = view.findViewById(R.id.inputCity);
        inputAddress = view.findViewById(R.id.inputAddress);

        selectImageButton = view.findViewById(R.id.selectImageButton);
        registerButton = view.findViewById(R.id.registerButton);
        selectedImageView = view.findViewById(R.id.selectedImageView);

        // Image picker button listener
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Register button listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });

        return view;
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
            selectedImageView.setImageURI(selectedImageUri);
        }
    }

    private void registerSeller() {
        String name = inputName.getText().toString().trim();
        String surname = inputSurname.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String repeatPassword = inputRepeatPassword.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String city = inputCity.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();

        // Validate fields and show Snackbar if needed
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