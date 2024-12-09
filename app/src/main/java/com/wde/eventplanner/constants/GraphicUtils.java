package com.wde.eventplanner.constants;

import android.content.res.Resources;
import android.util.TypedValue;

public class GraphicUtils {
    public static int dp2px(Resources r, int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
