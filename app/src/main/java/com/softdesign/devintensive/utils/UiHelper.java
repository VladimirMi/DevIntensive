package com.softdesign.devintensive.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.softdesign.devintensive.data.managers.DataManager;

public class UiHelper {
    private static Context sContext = DataManager.getInstance().getContext();

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = sContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = sContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarHeight() {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (sContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, sContext.getResources().getDisplayMetrics());
        }
        return result;
    }
}
