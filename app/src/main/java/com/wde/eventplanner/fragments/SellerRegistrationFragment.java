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
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.DemoUser;
import com.wde.eventplanner.R;

import java.util.ArrayList;
import java.util.List;

public class SellerRegistrationFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image selection
    private TextInputEditText inputName, inputSurname, inputEmail, inputPassword, inputRepeatPassword;
    private TextInputEditText inputCompanyName, inputPhone, inputDescription, inputCity, inputAddress;
    private MaterialButton selectImageButton, registerButton;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private List<DemoUser> userList;

    public SellerRegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_seller_registration_screen, container, false);

        // Initialize the userList
        userList = new ArrayList<>();
        userList.add(new DemoUser("email@email.com", "Faks1312!"));
        userList.add(new DemoUser("ftn@ftn.com", "Faks1312!"));
        userList.add(new DemoUser("a@a.com", "Faks1312!"));

        // Bind views
        inputName = rootView.findViewById(R.id.inputName);
        inputSurname = rootView.findViewById(R.id.inputSurname);
        inputEmail = rootView.findViewById(R.id.inputEmail);
        inputPassword = rootView.findViewById(R.id.inputPassword);
        inputRepeatPassword = rootView.findViewById(R.id.inputRepeatPassword);
        inputCompanyName = rootView.findViewById(R.id.inputCompanyName);
        inputPhone = rootView.findViewById(R.id.inputPhone);
        inputDescription = rootView.findViewById(R.id.inputDescription);
        inputCity = rootView.findViewById(R.id.inputCity);
        inputAddress = rootView.findViewById(R.id.inputAddress);

        selectImageButton = rootView.findViewById(R.id.selectImageButton);
        registerButton = rootView.findViewById(R.id.registerButton);
        selectedImageView = rootView.findViewById(R.id.selectedImageView);

        // Set up listeners
        selectImageButton.setOnClickListener(v -> openImagePicker());

        registerButton.setOnClickListener(v -> registerSeller());

        return rootView;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image selection
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
        String companyName = inputCompanyName.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();
        String city = inputCity.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();

        // Validate required fields (name, surname, email, password, repeatPassword, companyName, city)
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || companyName.isEmpty() || city.isEmpty()) {
            showToast("Please fill in all the required fields");
            return;
        }

        // Validate name and surname
        if (!name.matches(NAME_REGEX)) {
            showToast("Invalid name format");
            return;
        }
        if (!surname.matches(NAME_REGEX)) {
            showToast("Invalid surname format");
            return;
        }

        // Validate email format
        if (!email.matches(EMAIL_REGEX)) {
            showToast("Invalid email format");
            return;
        }

        // Check if user has an account
        if (!isValidUser(email)){
            showToast("There is an existing account with the same email.");
            return;
        }

        // Validate password strength
        if (!isStrongPassword(password)) {
            showToast("Password is too weak. Use 8+ chars, upper & lowercase, number, and special char.");
            return;
        }

        // Check if passwords match
        if (!password.equals(repeatPassword)) {
            showToast("Passwords do not match");
            return;
        }

        // Validate phone number format
        if (!phone.isEmpty() && !phone.matches(PHONE_REGEX)) {
            showToast("Invalid phone number format");
            return;
        }

        // Validate city format
        if (!city.matches(CITY_REGEX)) {
            showToast("Invalid city format");
            return;
        }

        // Validate address format
        if (!address.matches(ADDRESS_REGEX)) {
            showToast("Invalid address format");
            return;
        }

        // Description can be empty or a valid string
        if (!description.isEmpty() && description.length() < 10) {
            showToast("Description is too short. Please provide at least 10 characters.");
            return;
        }

        // Successful validation
        showToast("Successful registration. Confirmation email sent.");
        // Proceed with further registration logic, like saving data to a database, etc.
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
