package com.wde.eventplanner.fragments.common;

import static com.wde.eventplanner.constants.GraphicUtils.dp2px;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;
import com.wde.eventplanner.R;

public class CustomTabLayout extends TabLayout {
    public CustomTabLayout(@NonNull Context context) {
        super(context);
        setUp();
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    private void setUp() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ContextCompat.getColor(getContext(), R.color.edge2));
        drawable.setSize(dp2px(getResources(), 1), 1);
        LinearLayout layout = (LinearLayout) getChildAt(0);
        layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        layout.setDividerDrawable(drawable);
        setTabRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));
        setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.secondary));
        setTabTextColors(ContextCompat.getColor(getContext(), R.color.pale_text), ContextCompat.getColor(getContext(), R.color.text));
    }
}
