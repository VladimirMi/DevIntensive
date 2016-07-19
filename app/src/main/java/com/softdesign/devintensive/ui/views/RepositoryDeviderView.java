package com.softdesign.devintensive.ui.views;

import android.content.Context;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;

public class RepositoryDeviderView extends LinearLayout {

    public RepositoryDeviderView(Context context) {
        super(context);
        inflate(getContext(), R.layout.custom_repository_devider, this);
    }
}
