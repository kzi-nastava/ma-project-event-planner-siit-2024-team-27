package com.wde.eventplanner.fragments.seller.create_listing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCreateServiceBinding;

public class CreateServiceFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private FragmentCreateServiceBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image selection
    private Uri selectedImageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateServiceBinding.inflate(inflater, container, false);
        binding.selectImageButton.setOnClickListener(v -> openImagePicker());
        binding.createButton.setOnClickListener(v -> createService());
        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "Service";
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
            binding.selectedImageView.setImageURI(selectedImageUri);
        }
    }

    public void createService() {

    }
}
