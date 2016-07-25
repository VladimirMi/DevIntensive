package com.softdesign.devintensive.ui.fragments;

import android.content.Context;
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

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
//    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
//    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
//    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
//    @BindView(R.id.toolbar) Toolbar mToolbar;
    FloatingActionButton mFab;
    RelativeLayout mProfilePlaceholder;
    ImageView mProfilePhoto;
//    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;
//    @BindView(R.id.navigation_view) NavigationView mNavigationView;
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

    @BindViews({R.id.rating_txt, R.id.code_lines_txt, R.id.projects_txt})
    TextView[] mUserStatisticViews;

    private boolean mEditMode;
    private File mPhotoFile = null;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private Uri mOriginPhotoUri;
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
        }
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

//        mFab.setOnClickListener(this);
//        mProfilePlaceholder.setOnClickListener(this);

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
        loadUserStatistic();


        return view;
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
//        setSupportActionBar(mToolbar);
        ActionBar actionBar = mActivity.getSupportActionBar();

//        mAppBarParams = (AppBarLayout.LayoutParams) mActivity.mCollapsingToolbar.getLayoutParams();
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
//            mProfilePlaceholder.setVisibility(View.VISIBLE);
//            lockToolbar();
//            mActivity.mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
//            mFab.setImageResource(R.drawable.ic_check_24dp);
            requestFocus(mUserInfoViews.get(0));
        } else {
            for (EditText userValue : mUserInfoViews) {
                userValue.setFocusable(false);
                userValue.setEnabled(false);
            }
            for (ImageView actionIcon : mActionIcons) {
                actionIcon.setClickable(true);
            }
//            mProfilePlaceholder.setVisibility(View.INVISIBLE);
//            unlockToolbar();
//            mActivity.mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white));
//            mFab.setImageResource(R.drawable.ic_mode_edit_24dp);
            mNestedScrollView.scrollTo(0, 0);
        }

    }

    private void loadUserInfo() {
        List<String> userInfo = mPreferencesManager.loadUserInfo();
        for (int i = 0; i < userInfo.size(); i++) {
            mUserInfoViews.get(i).setText(userInfo.get(i));
        }
    }

    private void loadUserStatistic() {
        List<String> userStatistic = mPreferencesManager.loadUserStatistic();
        for (int i = 0; i < userStatistic.size(); i++) {
            mUserStatisticViews[i].setText(userStatistic.get(i));
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }
}
