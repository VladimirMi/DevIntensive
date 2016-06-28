package com.softdesign.devintensive.ui.view.behaviors;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.Converter;

public class UserRatingItemBeahavior extends CoordinatorLayout.Behavior<LinearLayout> {
    public static final String TAG = ConstantManager.TAG_PREFIX + "UserRatingItemBeahavior";
    private static final float sRatingItemPadding = Converter.convertDpToPixel(28);

    public UserRatingItemBeahavior() {
    }

    public UserRatingItemBeahavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        // один большой костыль
        float delta = dependency.getY() - 228;
        double speed = 4; // в пределах 1-6

        if (delta >= 0 && delta <= 84 * speed) {
            child.setPadding(0, (int) (delta/speed),
                    0, (int) (delta/speed));
            dependency.setPadding(dependency.getPaddingLeft(), child.getHeight(),
                    dependency.getPaddingRight(), dependency.getPaddingBottom());
        }

        child.setY(dependency.getY());
        return true;
    }

}
