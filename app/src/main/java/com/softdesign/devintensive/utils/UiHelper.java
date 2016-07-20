package com.softdesign.devintensive.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static void setUserPhoto(final Context context, final String path, final ImageView view) {
        Picasso.with(context)
                .load(path)
                .fit()
                .centerCrop()
                .placeholder(context.getResources().getDrawable(R.drawable.user_bg))
                .error(context.getResources().getDrawable(R.drawable.user_bg))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(path, "Load from cache");
                    }

                    @Override
                    public void onError() {
                        DataManager.getInstance().getPicasso()
                                .load(path)
                                .fit()
                                .centerCrop()
                                .placeholder(context.getResources().getDrawable(R.drawable.user_bg))
                                .error(context.getResources().getDrawable(R.drawable.user_bg))
                                .into(view, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(path, "Load from net");
                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(path, "Could not fetch the image");
                                    }
                                });
                    }
                });
    }

    public static void setUserAvatar(final Context context, final String path, final ImageView view) {
        Picasso.with(context)
                .load(path)
                .fit()
                .centerCrop()
                .transform(new CircleTransformation())
                .placeholder(context.getResources().getDrawable(R.drawable.user_bg))
                .error(context.getResources().getDrawable(R.drawable.user_bg))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(path, "Load from cache");
                    }

                    @Override
                    public void onError() {
                        DataManager.getInstance().getPicasso()
                                .load(path)
                                .fit()
                                .centerCrop()
                                .transform(new CircleTransformation())
                                .placeholder(context.getResources().getDrawable(R.drawable.user_bg))
                                .error(context.getResources().getDrawable(R.drawable.user_bg))
                                .into(view, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(path, "Load from net");
                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(path, "Could not fetch the image");
                                    }
                                });
                    }
                });
    }
}
