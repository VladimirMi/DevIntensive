package com.softdesign.devintensive.ui.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.UiHelper;

public class UserStatisticBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private final String TAG = ConstantManager.TAG_PREFIX + "UserStatisticBehavior";
    private final int mMaxAppBarHeight;
    private final int mMinAppBarHeight;
    private final int mInitialPadding;


    public UserStatisticBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        mMinAppBarHeight = UiHelper.getStatusBarHeight() + UiHelper.getActionBarHeight();
        mMaxAppBarHeight = context.getResources().getDimensionPixelOffset(R.dimen.profile_image_size);
        mInitialPadding = context.getResources().getDimensionPixelOffset(R.dimen.spacing_medium_28);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {

        int padding = ((int) dependency.getBottom() - mMinAppBarHeight) * mInitialPadding /
                (mMaxAppBarHeight - mMinAppBarHeight);

        child.setY(dependency.getBottom());
        child.setPadding(0, padding, 0, padding);

        NestedScrollView scrollView = (NestedScrollView) parent.getChildAt(2);
        scrollView.setPadding(scrollView.getPaddingLeft(), child.getHeight(),
                scrollView.getPaddingRight(), scrollView.getPaddingBottom());

        return super.onDependentViewChanged(parent, child, dependency);
    }
}

