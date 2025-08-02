package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wde.eventplanner.databinding.ItemCarouselImageBinding;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<String> images;

    public ImageAdapter(List<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCarouselImageBinding binding = ItemCarouselImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Picasso.get().load(images.get(position)).into(holder.binding.carouselImage);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ItemCarouselImageBinding binding;

        public ImageViewHolder(ItemCarouselImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}