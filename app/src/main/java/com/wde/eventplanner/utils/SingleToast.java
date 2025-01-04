package com.wde.eventplanner.utils;

import android.content.Context;
import android.widget.Toast;

public class SingleToast {
    private static Toast currentToast;

    public static void show(Context context, String message) {
        if (currentToast != null) currentToast.cancel();
        currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        currentToast.show();
    }
}
