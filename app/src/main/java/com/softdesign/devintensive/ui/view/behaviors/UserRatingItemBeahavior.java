package com.softdesign.devintensive.ui.view.behaviors;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

public class UserRatingItemBeahavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = ConstantManager.TAG_PREFIX + "UserRatingItemBeahavior";
    private boolean changeTopPadding = true;
    private boolean changeBottomPadding = false;


    public UserRatingItemBeahavior() {
    }

    public UserRatingItemBeahavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof NestedScrollView;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        int initialPadding = (int) parent.getResources().getDimension(R.dimen.spacing_medium_28);
        int topPointOfNestedScroll = (int) (parent.getResources().getDimension(R.dimen.size_small_24) +
                parent.getResources().getDimension(R.dimen.size_medium_56));
        int bottomPointOfNestedScroll = (int) parent.getResources().getDimension(R.dimen.profile_image_size);

        int padding = ((int) dependency.getY() - topPointOfNestedScroll) * initialPadding /
                (bottomPointOfNestedScroll - topPointOfNestedScroll);


        child.setPadding(0, padding, 0, padding);
        dependency.setPadding(dependency.getPaddingLeft(), child.getHeight(),
                dependency.getPaddingRight(), dependency.getPaddingBottom());

        child.setY(dependency.getY());
        return true;
    }

    /** Другой вариант поведения (не зависит от разметки)
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        int initialPadding = (int) coordinatorLayout.getResources().getDimension(R.dimen.spacing_medium_28);
        int paddingTop = child.getPaddingTop();
        int paddingBottom = child.getPaddingBottom();

        if (changeTopPadding && dyConsumed != 0) {
            paddingTop = paddingTop - dyConsumed;
            if (paddingTop < 0) paddingTop = 0;
            if (paddingTop > initialPadding) paddingTop = initialPadding;
            child.setPadding(0, paddingTop,
                    0, child.getPaddingBottom());
            if (paddingTop == 0) {
                changeTopPadding = false;
                changeBottomPadding = true;
            }
        }

        if (changeBottomPadding && dyConsumed != 0) {
            paddingBottom = paddingBottom - dyConsumed;
            if (paddingBottom < 0) paddingBottom = 0;
            if (paddingBottom > initialPadding) paddingBottom = initialPadding;
            child.setPadding(0, child.getPaddingTop(),
                    0, paddingBottom);
            if (paddingBottom == initialPadding) {
                changeTopPadding = true;
                changeBottomPadding = false;
            }
        }
        target.setPadding(target.getPaddingLeft(), child.getHeight(),
                target.getPaddingRight(), target.getPaddingBottom());
    }
    */
}

