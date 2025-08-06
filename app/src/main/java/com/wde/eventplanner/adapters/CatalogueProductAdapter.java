package com.wde.eventplanner.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardCatalogueItemBinding;
import com.wde.eventplanner.models.products.CatalogueProduct;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

public class CatalogueProductAdapter extends RecyclerView.Adapter<CatalogueProductAdapter.CommentViewHolder> {
    public final ArrayList<CatalogueProduct> items;

    public CatalogueProductAdapter(ArrayList<CatalogueProduct> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardCatalogueItemBinding binding = CardCatalogueItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CatalogueProduct item = items.get(position);
        holder.binding.name.setText(item.getName());
        holder.binding.price.setText(String.format(Locale.US, "%.1f", (1 - item.getSalePercentage()) * item.getPrice()));
        holder.binding.oldPrice.setText(String.format(Locale.US, "%d", item.getPrice().intValue()));
        holder.binding.discount.setText(String.format(Locale.US, "%d", Double.valueOf(item.getSalePercentage() * 100).intValue()));

        if (holder.priceWatcher != null)
            holder.binding.oldPrice.removeTextChangedListener(holder.priceWatcher);
        if (holder.discountWatcher != null)
            holder.binding.discount.removeTextChangedListener(holder.discountWatcher);

        Runnable refresh = () -> holder.binding.price.setText(String.format(Locale.US, "%.1f", (1 - item.getSalePercentage()) * item.getPrice()));

        holder.priceWatcher = new CustomTextWatcher(item::setPrice, refresh);
        holder.discountWatcher = new CustomTextWatcher(discount -> item.setSalePercentage(discount / 100), refresh);

        holder.binding.oldPrice.addTextChangedListener(holder.priceWatcher);
        holder.binding.discount.addTextChangedListener(holder.discountWatcher);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        CardCatalogueItemBinding binding;
        public TextWatcher priceWatcher;
        public TextWatcher discountWatcher;

        public CommentViewHolder(CardCatalogueItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class CustomTextWatcher implements TextWatcher {
        private final Consumer<Double> consumer;
        private final Runnable refresh;

        private CustomTextWatcher(Consumer<Double> consumer, Runnable refresh) {
            this.consumer = consumer;
            this.refresh = refresh;
        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            consumer.accept(s.toString().isBlank() ? 0 : Double.parseDouble(s.toString()));
            refresh.run();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}