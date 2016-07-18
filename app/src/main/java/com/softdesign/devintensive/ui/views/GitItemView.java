package com.softdesign.devintensive.ui.views;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GitItemView extends LinearLayout {
    @BindView(R.id.github_txt) TextView mGitTextView;
    @BindView(R.id.github_img) ImageView mGitImageView;

    public GitItemView(Context context, String repo) {
        super(context);
        inflate(getContext(), R.layout.item_repositories_list, this);

        ButterKnife.bind(this);

        mGitTextView.setText(repo);
        mGitImageView.setContentDescription(repo);
    }

    public ImageView getGitImageView() {
        return mGitImageView;
    }
}
