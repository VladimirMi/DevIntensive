package com.softdesign.devintensive.ui.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.UiHelper;

public class UserStatisticBehavior extends AppBarLayout.ScrollingViewBehavior {
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
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        int padding = ((int) dependency.getBottom() - mMinAppBarHeight) * mInitialPadding /
                (mMaxAppBarHeight - mMinAppBarHeight);

        child.setPadding(0, padding, 0, padding);

        FrameLayout fragmentContent = (FrameLayout) parent.getChildAt(2);

        if (child.getVisibility()==View.VISIBLE) {
            fragmentContent.setPadding(0, child.getHeight(), 0, 0);
        }

        return super.onDependentViewChanged(parent, child, dependency);
    }
}

