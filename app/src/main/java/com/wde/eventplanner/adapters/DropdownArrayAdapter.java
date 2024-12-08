package com.wde.eventplanner.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DropdownArrayAdapter extends ArrayAdapter<String> {
    private final List<String> values;

    public DropdownArrayAdapter(Context context, List<String> values) {
        super(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(values));
        this.values = values;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> filtered;
                if (constraint != null && constraint.length() != 0)
                    filtered = values.stream().filter(value -> value.toLowerCase().startsWith(constraint.toString().trim().toLowerCase())).collect(Collectors.toList());
                else
                    filtered = new ArrayList<>(values);
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                addAll((List<String>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
