package com.wde.eventplanner.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.R;

public class SellerCreateServiceFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image selection
    private TextInputEditText inputName, inputPrice, inputDiscount, inputProductCategory, inputEventCategories;
    private TextInputEditText inputReservationPeriod, inputCancellationPeriod, inputConfirmationType, inputVisibility, inputAvailability;
    private TextInputEditText inputMinDuration, inputMaxDuration, inputDescription;
    private MaterialButton selectImageButton, createButton;
    private ImageView selectedImageView;
    private Uri selectedImageUri;

    public SellerCreateServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_seller_create_service_screen, container, false);

        // Bind views
        inputName = rootView.findViewById(R.id.inputName);
        inputPrice = rootView.findViewById(R.id.inputPrice);
        inputDiscount = rootView.findViewById(R.id.inputDiscount);
        inputProductCategory = rootView.findViewById(R.id.inputProductCategory);
        inputEventCategories = rootView.findViewById(R.id.inputEventCategories);
        inputReservationPeriod = rootView.findViewById(R.id.inputReservationPeriod);
        inputCancellationPeriod = rootView.findViewById(R.id.inputCancellationPeriod);
        inputConfirmationType = rootView.findViewById(R.id.inputConfirmationType);
        inputVisibility = rootView.findViewById(R.id.inputVisibility);
        inputAvailability = rootView.findViewById(R.id.inputAvailability);
        inputMinDuration = rootView.findViewById(R.id.inputMinDuration);
        inputMaxDuration = rootView.findViewById(R.id.inputMaxDuration);
        inputDescription = rootView.findViewById(R.id.inputDescription);

        selectImageButton = rootView.findViewById(R.id.selectImageButton);
        createButton = rootView.findViewById(R.id.createButton);
        selectedImageView = rootView.findViewById(R.id.selectedImageView);

        // Set up listeners
        selectImageButton.setOnClickListener(v -> openImagePicker());

        createButton.setOnClickListener(v -> createService());

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

    public void createService() {

    }
}
