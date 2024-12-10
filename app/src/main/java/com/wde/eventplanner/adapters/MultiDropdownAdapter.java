package com.wde.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.wde.eventplanner.R;
import com.wde.eventplanner.components.MultiDropDown;

import java.util.ArrayList;

public class MultiDropdownAdapter<T extends MultiDropDown.MultiDropDownItem<?>> extends ArrayAdapter<T> {
    public final ArrayList<T> items;
    private final Context context;

    public MultiDropdownAdapter(Context context, ArrayList<T> items) {
        super(context, R.layout.item_multi_dropdown, new ArrayList<>(items));
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_multi_dropdown, parent, false);
        }

        ((TextView) view.findViewById(R.id.textView)).setText(items.get(position).name);
        ((MaterialCheckBox) view.findViewById(R.id.checkBox)).setChecked(items.get(position).checked);

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.count = items.size();
                results.values = items;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }
}