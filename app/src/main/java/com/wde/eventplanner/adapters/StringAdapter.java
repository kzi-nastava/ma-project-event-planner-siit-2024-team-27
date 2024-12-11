package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.ItemDropdownBinding;

import java.util.List;

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.StringHolder> {

    private List<String> strings;

    public StringAdapter(List<String> strings) {
        this.strings = strings;
    }

    @NonNull
    @Override
    public StringAdapter.StringHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDropdownBinding binding = ItemDropdownBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StringAdapter.StringHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StringAdapter.StringHolder holder, int position) {
        String string = strings.get(position);
        holder.binding.text1.setText(string);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public static class StringHolder extends RecyclerView.ViewHolder {
        ItemDropdownBinding binding;

        public StringHolder(ItemDropdownBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
