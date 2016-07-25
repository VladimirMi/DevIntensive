package com.softdesign.devintensive.ui.views;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepositoryView extends LinearLayout implements View.OnFocusChangeListener {
    private final CustomClickListener mListener;
    @BindView(R.id.github_et) EditText mGitEditText;
    @BindView(R.id.github_img) ImageView mGitImage;

    public RepositoryView(Context context, CustomClickListener listener) {
        super(context);
        inflate(context, R.layout.item_repositories_list, this);
        ButterKnife.bind(this);

        mListener = listener;
        mGitEditText.setOnFocusChangeListener(this);
    }

    @OnClick(R.id.github_img)
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onIconClickListener(Uri.parse(ConstantManager.HTTPS_SCHEME + mGitEditText.getText()));
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String value = String.valueOf(mGitEditText.getText());
        int startIndex = value.indexOf("github");
        if (startIndex > 0) {
            mGitEditText.setText(value.substring(startIndex));
        }
    }

    public EditText getGitEditText() {
        return mGitEditText;
    }

    public ImageView getGitImage() {
        return mGitImage;
    }

    public interface CustomClickListener {
        void onIconClickListener(Uri uri);
    }
}
