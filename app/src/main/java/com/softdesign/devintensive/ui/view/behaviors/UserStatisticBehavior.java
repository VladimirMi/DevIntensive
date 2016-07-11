package com.softdesign.devintensive.ui.view.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

public class UserStatisticBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private static final String TAG = ConstantManager.TAG_PREFIX + "UserStatisticBehavior";
    private boolean changeTopPadding = true;
    private boolean changeBottomPadding = false;
    private int mInitialPadding;

    public UserStatisticBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInitialPadding = context.getResources().getDimensionPixelSize(R.dimen.spacing_medium_28);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof AppBarLayout;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        // привязка плашки к нижнему краю AppBar
        child.setY(dependency.getBottom());
        return true;
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        int paddingTop = child.getPaddingTop();
        int paddingBottom = child.getPaddingBottom();

        // Движение вверх
        if (dyConsumed > 0) {
            if (changeTopPadding) {
                paddingTop = paddingTop - dyConsumed;
                setPaddingTop(child, paddingTop);
            }
            if (changeBottomPadding) {
                paddingBottom = paddingBottom - dyConsumed;
                setPaddingBottom(child, paddingBottom);
            }
        }
        // Движение вниз
        if (dyUnconsumed < 0) {
            if (changeTopPadding) {
                paddingTop = paddingTop - dyUnconsumed;
                setPaddingTop(child, paddingTop);
            }
            if (changeBottomPadding) {
                paddingBottom = paddingBottom - dyUnconsumed;
                setPaddingBottom(child, paddingBottom);
            }
        }
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        // установка соответствующего заполнения для NestedScrollView
        target.setPadding(target.getPaddingLeft(), child.getHeight(),
                target.getPaddingRight(), target.getPaddingBottom());
    }


    /**
     * Установка верхнего заполнения
     * @param linearLayout цель
     * @param padding новое значение
     */
    private void setPaddingTop(LinearLayout linearLayout, int padding) {
        if (padding < 0) padding = 0;
        if (padding > mInitialPadding) padding = mInitialPadding;
        linearLayout.setPadding(0, padding,
                0, linearLayout.getPaddingBottom());
        if (padding == 0) {
            changeTopPadding = false;
            changeBottomPadding = true;
        }
    }

    /**
     * Установка нижнего заполнения
     * @param linearLayout цель
     * @param padding новое значение
     */
    private void setPaddingBottom(LinearLayout linearLayout, int padding) {
        if (padding < 0) padding = 0;
        if (padding > mInitialPadding) padding = mInitialPadding;
        linearLayout.setPadding(0, linearLayout.getPaddingTop(),
                0, padding);
        if (padding == mInitialPadding) {
            changeTopPadding = true;
            changeBottomPadding = false;
        }
    }

}

