package com.softdesign.devintensive.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.activities.MainActivity;
import com.softdesign.devintensive.ui.views.RepositoryDeviderView;
import com.softdesign.devintensive.ui.views.RepositoryView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.MyTextWatcher;
import com.softdesign.devintensive.utils.UiHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class UserProfileFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener{

    private static final String TAG = ConstantManager.TAG_PREFIX + "UserProfileFragment";

    @BindView(R.id.nested_scroll) NestedScrollView mNestedScrollView;
    @BindView(R.id.repositories_list) LinearLayout mRepoListView;

    @BindView(R.id.phone_et) EditText mUserPhone;
    @BindView(R.id.email_et) EditText mUserEmail;
    @BindView(R.id.vk_et) EditText mUserVk;
    @BindView(R.id.bio_et) EditText mUserBio;

    @BindView(R.id.make_call_img) ImageView mPhoneIcon;
    @BindView(R.id.send_email_img) ImageView mEmailIcon;
    @BindView(R.id.vk_img) ImageView mVkIcon;

    private List<EditText> mUserInfoViews;

    private List<ImageView> mActionIcons;

    private boolean mEditMode;

    private MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
            mEditMode = mActivity.mEditMode;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);

        mActionIcons = new ArrayList<>();
        mActionIcons.add(mPhoneIcon);
        mActionIcons.add(mEmailIcon);
        mActionIcons.add(mVkIcon);

        for (View actionIcon : mActionIcons) {
            actionIcon.setOnClickListener(this);
        }

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserEmail);
        mUserInfoViews.add(mUserVk);
        initRepositoriesView();
        mUserInfoViews.add(mUserBio);


        for (int i = 0; i < mUserInfoViews.size() - 1; i++) {
            mUserInfoViews.get(i).addTextChangedListener(new MyTextWatcher(mActivity,
                    mUserInfoViews.get(i), mActionIcons.get(i)));
        }

        if (savedInstanceState != null) {
            mEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
        }

        setupToolbar();
        setupEditMode();

        loadUserInfo();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveUserInfo();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        loadUserInfo();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            // отсекает лишние символы перед "vk"
            case R.id.vk_et:
                String value = mUserVk.getText().toString();
                int startIndex = value.indexOf("vk");
                if (startIndex > 0) {
                    mUserVk.setText(value.substring(startIndex));
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.make_call_img:
                makeActionView(Uri.parse(ConstantManager.TELEPHONE_SCHEME + mUserPhone.getText()));
                break;
            case R.id.send_email_img:
                sendEmail(Uri.parse(ConstantManager.MAIL_SCHEME + mUserEmail.getText()));
                break;
            case R.id.vk_img:
                makeActionView(Uri.parse(ConstantManager.HTTPS_SCHEME + mUserVk.getText()));
                break;
        }
    }

    private void initRepositoriesView() {

        for (int i = 0; i < mPreferencesManager.getRepositoriesSize(); i++) {
            RepositoryView repositoryView = new RepositoryView(mActivity, new RepositoryView.CustomClickListener() {
                @Override
                public void onIconClickListener(Uri uri) {
                    makeActionView(uri);
                }
            });
            mRepoListView.addView(repositoryView);

            if (i < mPreferencesManager.getRepositoriesSize() - 1) {
                RepositoryDeviderView deviderView = new RepositoryDeviderView(mActivity);
                mRepoListView.addView(deviderView);
            }
            mUserInfoViews.add(repositoryView.getGitEditText());
            mActionIcons.add(repositoryView.getGitImage());
        }
    }

    private void setupToolbar() {
        ActionBar actionBar = mActivity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mPreferencesManager.loadUserName());
        }
    }


    /**
     * переключает режим редактирования
     */
    public void setupEditMode() {
        if (mEditMode) {
            for (EditText userValue : mUserInfoViews) {
                userValue.setFocusableInTouchMode(true);
                userValue.setFocusable(true);
                userValue.setEnabled(true);
            }
            for (ImageView actionIcon : mActionIcons) {
                actionIcon.setClickable(false);
            }
            requestFocus(mUserInfoViews.get(0));
        } else {
            for (EditText userValue : mUserInfoViews) {
                userValue.setFocusable(false);
                userValue.setEnabled(false);
            }
            for (ImageView actionIcon : mActionIcons) {
                actionIcon.setClickable(true);
            }
            mNestedScrollView.scrollTo(0, 0);
        }

    }

    private void loadUserInfo() {
        List<String> userInfo = mPreferencesManager.loadUserInfo();
        for (int i = 0; i < userInfo.size(); i++) {
            mUserInfoViews.get(i).setText(userInfo.get(i));
        }
    }


    /**
     * Запрос фокуса у EditText и если возможно установка его и перемещение курсора
     * в конец строки
     *
     * @param editText EditText у которого запрашивается фокус
     */
    public void requestFocus(EditText editText) {
        if (editText.requestFocus()) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            editText.setSelection(editText.length());
        }
    }

    /**
     * Вызывается при клике на FAB в MainActivity
     * @return true - если режим редактирования сменился, false - если нет.
     */
    public boolean onFabClick() {
        if (mEditMode) {
            for (EditText userInfoView : mUserInfoViews) {
                if (!MyTextWatcher.isValid(userInfoView)) {
                    requestFocus(userInfoView);
                    return false;
                }
            }
            mEditMode = false;
            setupEditMode();
        } else {
            mEditMode = true;
            setupEditMode();
        }
        return true;
    }

    private void saveUserInfo() {
        List<String> userInfo = new ArrayList<>();

        StringBuilder repositories = new StringBuilder();
        for (EditText userInfoView : mUserInfoViews) {
            if (userInfoView.getTag().equals(getString(R.string.git_tag))) {
                repositories.append(userInfoView.getText().toString()).append(" ");
            } else {
                userInfo.add(userInfoView.getText().toString());
            }
        }
        userInfo.add(3, repositories.toString());
        mPreferencesManager.saveUserInfo(userInfo);
    }
}
