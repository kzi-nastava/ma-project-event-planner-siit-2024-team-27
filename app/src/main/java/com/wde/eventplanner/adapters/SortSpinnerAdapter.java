package com.wde.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wde.eventplanner.R;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SortSpinnerAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] items;
    private final AtomicInteger selectedPosition;
    private final AtomicBoolean orderDesc;

    public SortSpinnerAdapter(Context context, String[] items, AtomicInteger selectedPosition, AtomicBoolean orderDesc) {
        super(context, R.layout.sort_spinner_item, items);
        this.selectedPosition = selectedPosition;
        this.orderDesc = orderDesc;
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sort_spinner_item, parent, false);
        }

        TextView textView = view.findViewById(R.id.spinnerItemText);
        ImageView imageView = view.findViewById(R.id.spinnerItemIcon);

        textView.setText(items[position]);

        if (position == selectedPosition.get())
            imageView.setImageResource(orderDesc.get() ? R.drawable.ic_sort_desc : R.drawable.ic_sort_asc);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
