package com.wde.eventplanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.ItemCarouselImageBinding;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<Color> colors;
    private Context context;

    public ImageAdapter(Context context, List<Color> colors) {
        this.context = context;
        this.colors = colors;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCarouselImageBinding binding = ItemCarouselImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Color color = colors.get(position);
        holder.binding.carouselImage.setBackgroundColor(color.toArgb());
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ItemCarouselImageBinding binding;

        public ImageViewHolder(ItemCarouselImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}