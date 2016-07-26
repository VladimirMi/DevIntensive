package com.softdesign.devintensive.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class UserStatisticFragment extends BaseFragment {

    @BindViews({R.id.rating_txt, R.id.code_lines_txt, R.id.projects_txt})
    List<TextView> mUserStatisticViews;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_statistic, container, false);
        ButterKnife.bind(this, view);

        loadUserStatistic();
        return view;
    }

    private void loadUserStatistic() {
        List<String> userStatistic = mPreferencesManager.loadUserStatistic();
        for (int i = 0; i < userStatistic.size(); i++) {
            mUserStatisticViews.get(i).setText(userStatistic.get(i));
        }
    }
}
