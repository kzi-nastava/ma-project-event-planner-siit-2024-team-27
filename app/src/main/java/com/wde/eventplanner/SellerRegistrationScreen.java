package com.wde.eventplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class SellerRegistrationScreen extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image selection

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    private TextInputEditText inputName, inputSurname, inputEmail, inputPassword, inputRepeatPassword;
    private TextInputEditText inputCompanyName, inputPhone, inputDescription, inputCity, inputAddress;
    private MaterialButton selectImageButton, registerButton;
    private ImageView selectedImageView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration_screen);

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
                showSnackbar("Seller image clicked");
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

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            showSnackbar("Please fill in all the required fields");
            return;
        }

        if (!password.equals(repeatPassword)) {
            showSnackbar("Passwords do not match");
            return;
        }
        //TODO expand required list, add regex checks, add check if there is an user with the same email, weak password
        showSnackbar("Successful registration. Confirmation email sent");
    }
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG).show();
    }
}