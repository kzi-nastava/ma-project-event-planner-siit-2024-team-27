package com.wde.eventplanner.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

public class DropdownArrayAdapter<T> extends ArrayAdapter<T> {
    private final T[] values;

    public DropdownArrayAdapter(Context context, T[] values) {
        super(context, android.R.layout.simple_spinner_dropdown_item, values);
        this.values = values;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.count = values.length;
                results.values = values;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }
}
