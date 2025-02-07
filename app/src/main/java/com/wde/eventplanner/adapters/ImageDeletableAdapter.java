package com.wde.eventplanner.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.ItemCarouselImageDeletableBinding;

import java.util.List;
import java.util.function.Function;

public class ImageDeletableAdapter extends RecyclerView.Adapter<ImageDeletableAdapter.ImageViewHolder> {
    private final ActivityResultLauncher<Intent> imagePickerLauncher;
    private final List<Uri> images;

    public ImageDeletableAdapter(Fragment fragment, List<Uri> images) {
        this.images = images;

        imagePickerLauncher = fragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
                addImage(result.getData().getData());
        });
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCarouselImageDeletableBinding binding = ItemCarouselImageDeletableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (position == images.size()) {
            holder.binding.selectedImageView.setOnClickListener(v -> openImagePicker());
            holder.binding.selectedImageView.setImageResource(R.drawable.add_image);
            holder.binding.removeImageButton.setVisibility(View.GONE);
        } else {
            holder.binding.removeImageButton.setOnClickListener(v -> removeImage(images.get(position)));
            holder.binding.selectedImageView.setOnClickListener(Function.identity()::apply);
            holder.binding.selectedImageView.setImageURI(images.get(position));
            holder.binding.removeImageButton.setVisibility(View.VISIBLE);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addImage(Uri uri) {
        images.add(uri);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void removeImage(Uri uri) {
        images.remove(uri);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return images.size() + 1;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ItemCarouselImageDeletableBinding binding;

        public ImageViewHolder(ItemCarouselImageDeletableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}