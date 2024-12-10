package com.wde.eventplanner.components;

import static com.wde.eventplanner.components.CustomGraphicUtils.dp2px;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.R;

public class ItemDividerDecoration extends RecyclerView.ItemDecoration {
    private final Context context;
    private final Resources resources;

    public ItemDividerDecoration(Context context, Resources resources) {
        this.resources = resources;
        this.context = context;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.edge2));
        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom() + ((RecyclerView.LayoutParams) child.getLayoutParams()).bottomMargin;
            c.drawRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), top + dp2px(resources, 1), paint);
        }
    }
}