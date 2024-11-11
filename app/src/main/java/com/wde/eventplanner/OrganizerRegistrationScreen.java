package com.wde.eventplanner;

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
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class OrganizerRegistrationScreen extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image selection
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    private TextInputEditText inputName, inputSurname, inputEmail, inputPassword, inputRepeatPassword;
    private TextInputEditText inputCompanyName, inputPhone, inputDescription, inputCity, inputAddress;
    private MaterialButton selectImageButton, registerButton;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private List<DemoUser> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_registration_screen);

        userList = new ArrayList<>();
        userList.add(new DemoUser("email@email.com", "Faks1312!"));
        userList.add(new DemoUser("ftn@ftn.com", "Faks1312!"));
        userList.add(new DemoUser("a@a.com", "Faks1312!"));

        inputName = findViewById(R.id.inputName);
        inputSurname = findViewById(R.id.inputSurname);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputRepeatPassword = findViewById(R.id.inputRepeatPassword);
        inputCompanyName = findViewById(R.id.inputCompanyName);
        inputPhone = findViewById(R.id.inputPhone);
        inputDescription = findViewById(R.id.inputDescription);
        inputCity = findViewById(R.id.inputCity);
        inputAddress = findViewById(R.id.inputAddress);

        selectImageButton = findViewById(R.id.selectImageButton);
        registerButton = findViewById(R.id.registerButton);
        selectedImageView = findViewById(R.id.selectedImageView);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showSnackbar("Seller image clicked");
                openImagePicker();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
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
            showSnackbar("Please fill in all the required fields");
            return;
        }

        // Validate name and surname
        if (!name.matches(NAME_REGEX)) {
            showSnackbar("Invalid name format");
            return;
        }
        if (!surname.matches(NAME_REGEX)) {
            showSnackbar("Invalid surname format");
            return;
        }

        // Validate email format
        if (!email.matches(EMAIL_REGEX)) {
            showSnackbar("Invalid email format");
            return;
        }

        //check if user has account
        if (!isValidUser(email)){
            showSnackbar("There is an existing account with the same email.");
            return;
        }

        // Validate password strength
        if (!isStrongPassword(password)) {
            showSnackbar("Password is too weak. Use 8+ chars, upper & lowercase, number, and special char.");
            return;
        }

        // Check if passwords match
        if (!password.equals(repeatPassword)) {
            showSnackbar("Passwords do not match");
            return;
        }

        // Validate phone number format
        if (!phone.isEmpty() && !phone.matches(PHONE_REGEX)) {
            showSnackbar("Invalid phone number format");
            return;
        }

        // Validate city format
        if (!city.matches(CITY_REGEX)) {
            showSnackbar("Invalid city format");
            return;
        }

        // Validate address format
        if (!address.matches(ADDRESS_REGEX)) {
            showSnackbar("Invalid address format");
            return;
        }

        // Description can be empty or a valid string
        if (!description.isEmpty() && description.length() < 10) {
            showSnackbar("Description is too short. Please provide at least 10 characters.");
            return;
        }

        // Successful validation
        showSnackbar("Successful registration. Confirmation email sent.");
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
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG).show();
    }
}
