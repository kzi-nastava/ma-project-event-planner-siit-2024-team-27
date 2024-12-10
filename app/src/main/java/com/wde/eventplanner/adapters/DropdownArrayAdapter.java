package com.wde.eventplanner.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import com.wde.eventplanner.R;
import com.wde.eventplanner.components.CustomDropDown.CustomDropDownItem;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DropdownArrayAdapter<T extends CustomDropDownItem<?>> extends ArrayAdapter<T> {
    private final ArrayList<T> values;
    public boolean ignoreFiltering;

    public DropdownArrayAdapter(Context context, ArrayList<T> values, boolean ignoreFiltering) {
        super(context, R.layout.item_dropdown, new ArrayList<>(values));
        this.ignoreFiltering = ignoreFiltering;
        this.values = values;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Filter.FilterResults results = new Filter.FilterResults();
                ArrayList<T> filtered;
                if (ignoreFiltering || constraint == null || constraint.toString().trim().isEmpty())
                    filtered = new ArrayList<>(values);
                else
                    filtered = values.stream().filter(value -> value.name.toLowerCase()
                            .startsWith(constraint.toString().trim().toLowerCase())).collect(Collectors.toCollection(ArrayList::new));

                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                clear();
                addAll((ArrayList<T>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}