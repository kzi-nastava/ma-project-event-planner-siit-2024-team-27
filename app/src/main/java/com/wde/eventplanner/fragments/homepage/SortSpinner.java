package com.wde.eventplanner.fragments.homepage;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

public class SortSpinner extends AppCompatSpinner {
    OnItemSelectedListener listener;

    public SortSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(null, null, position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
}