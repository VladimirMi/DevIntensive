package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.CustomGlideModule;
import com.softdesign.devintensive.data.network.res.UploadImageRes;
import com.softdesign.devintensive.ui.fragments.UserListFragment;
import com.softdesign.devintensive.ui.fragments.UserProfileFragment;
import com.softdesign.devintensive.ui.fragments.UserStatisticFragment;
import com.softdesign.devintensive.ui.views.RepositoryDeviderView;
import com.softdesign.devintensive.ui.views.RepositoryView;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ExStorageState;
import com.softdesign.devintensive.utils.Helper;
import com.softdesign.devintensive.utils.MyTextWatcher;
import com.softdesign.devintensive.utils.UiHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.appbar_layout) public AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar) public CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.profile_placeholder) RelativeLayout mProfilePlaceholder;
    @BindView(R.id.profile_photo) ImageView mProfilePhoto;

    @BindView(R.id.fragment_statistic_container) FrameLayout mStatisticContainer;
    @BindView(R.id.profile_photo_container) FrameLayout mPhotoContainer;


    public boolean mEditMode;
    private File mPhotoFile = null;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private Uri mOriginPhotoUri;

    private FragmentManager mFragmentManager;
    private UserProfileFragment mUserProfileFragment;
    private UserStatisticFragment mUserStatisticFragment;
    private UserListFragment mUserListFragment;
    private MenuItem mSearchItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        ButterKnife.bind(this);

        mFragmentManager = getFragmentManager();

        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        setupEditMode();

        loadUserPhoto();
        if (savedInstanceState == null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            mUserStatisticFragment = new UserStatisticFragment();
            mUserProfileFragment = new UserProfileFragment();

            transaction.add(R.id.fragment_statistic_container, mUserStatisticFragment);
            transaction.replace(R.id.fragment_content_container, mUserProfileFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            mNavigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.fab:
                if (mUserProfileFragment.onFabClick()) {
                    if (mEditMode) {
                        if (mPhotoFile != null) {
                            Uri editorPhotoUri = Uri.fromFile(mPhotoFile);
                            if (!mOriginPhotoUri.equals(editorPhotoUri)) {
                                uploadPhoto();
                            }
                        }
                        mEditMode = false;
                        setupEditMode();
                    } else {
                        mOriginPhotoUri = Uri.parse(mPreferencesManager.loadUserPhoto());
                        mEditMode = true;
                        setupEditMode();
                    }
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mEditMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else if (mEditMode) {
            mFab.performClick();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantManager.GALLERY_PERMISSION_REQUEST_CODE:
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        showPermissionSnackbar();
                        return;
                    }
                }
                loadPhotoFromGallery();
                break;
            case ConstantManager.CAMERA_PERMISSION_REQUEST_CODE:
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        showPermissionSnackbar();
                        return;
                    }
                }
                loadPhotoFromCamera();
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_diallog_camera),
                        getString(R.string.user_profile_dialog_cancel)};

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.user_profile_dialog_title);
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                dialogInterface.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
    }

    private void setupDrawer() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.user_profile_menu:
                        unlockToolbar();
                        expandToolbar();
                        mPhotoContainer.setVisibility(View.VISIBLE);
                        mSearchItem.setVisible(false);
                        FragmentTransaction transaction = mFragmentManager.beginTransaction();
                        mUserStatisticFragment = new UserStatisticFragment();
                        mUserProfileFragment = new UserProfileFragment();

                        transaction.add(R.id.fragment_statistic_container, mUserStatisticFragment);
                        transaction.replace(R.id.fragment_content_container, mUserProfileFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                    case R.id.team_menu:
                        collapseToolbar();
                        lockToolbar();
                        mSearchItem.setVisible(true);
                        transaction = mFragmentManager.beginTransaction();
                        mUserListFragment = new UserListFragment();

                        transaction.remove(mUserStatisticFragment);
                        transaction.replace(R.id.fragment_content_container, mUserListFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                    case R.id.login_menu:
                        mPreferencesManager.clearAllData();
                        mPreferencesManager.saveAuthToken(ConstantManager.INVALID_TOKEN);
                        Intent loginIntent = new Intent(MainActivity.this, AuthActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                        finish();
                        return true;
                }
                return false;
            }
        });

        // установка круглого аватара
        final ImageView userAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        CustomGlideModule.setUserAvatar(this, mPreferencesManager.loadUserAvatar(), userAvatar);

        TextView userName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name_txt);
        TextView userEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_email_txt);
        userName.setText(mPreferencesManager.loadUserName());
        userEmail.setText(mPreferencesManager.loadUserInfo().get(1));
    }

    /**
     * переключает режим редактирования
     */
    private void setupEditMode() {
        if (mEditMode) {

            mProfilePlaceholder.setVisibility(View.VISIBLE);
//            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            mFab.setImageResource(R.drawable.ic_check_24dp);
        } else {

            mProfilePlaceholder.setVisibility(View.INVISIBLE);
//            unlockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white));
            mFab.setImageResource(R.drawable.ic_mode_edit_24dp);
        }
    }

    private void loadUserPhoto() {
        CustomGlideModule.setUserPhoto(this, mPreferencesManager.loadUserPhoto(), mProfilePhoto);
    }


    private void loadPhotoFromGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {

            Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            takeGalleryIntent.setType(ConstantManager.MIME_TYPE_IMAGE);
            startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.chooser_gallery)),
                    ConstantManager.REQUEST_GALLERY_PICTURE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantManager.GALLERY_PERMISSION_REQUEST_CODE);
        }
    }

    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = Helper.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(Intent.createChooser(takeCaptureIntent, getString(R.string.chooser_camera)),
                        ConstantManager.REQUEST_CAMERA_PICTURE);
            } else {
                showSnackBar(getString(R.string.err_msg_ext_storage));
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Получение рузультата из другой Activity (фото из галлереи или камеры)
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImage;
        switch (requestCode) {
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (mPhotoFile != null && resultCode == RESULT_OK) {
                    selectedImage = Uri.fromFile(mPhotoFile);
                    mPreferencesManager.saveUserPhoto(selectedImage);
                    loadUserPhoto();
                }
                break;
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (data != null && resultCode == RESULT_OK) {
                    selectedImage = data.getData();
                    mPhotoFile = new File(selectedImage.getPath());
                    mPreferencesManager.saveUserPhoto(selectedImage);
                    loadUserPhoto();
                }
                break;
        }
    }

    private void expandToolbar() {
        mAppBarLayout.setExpanded(true, true);
    }

    private void collapseToolbar() {
        mAppBarLayout.setExpanded(false, true);
    }

    private void lockToolbar() {
        //убирает конфликт с анимацией
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAppBarParams.setScrollFlags(0);
            }
        }, 1000);

        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void showPermissionSnackbar() {
        Snackbar.make(mCoordinatorLayout, getString(R.string.permission_snackbar_text), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.permission_snackbar_title), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openApplicationSettings();
                    }
                }).show();
    }

    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse(ConstantManager.PACKAGE_SCHEME + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.SETTINGS_PERMISSION_REQUEST_CODE);
    }

    private void uploadPhoto() {
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(ConstantManager.CONTENT_TYPE_MULTIPART), mPhotoFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData(
                AppConfig.PHOTO_FORM_KEY, mPhotoFile.getName(), requestFile);

        Call<UploadImageRes> call = mDataManager.uploadPhoto(mPreferencesManager.getUserId(), body);
        call.enqueue(new Callback<UploadImageRes>() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "uploadPhoto onResponse");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG, "uploadPhoto onFailure");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        mSearchItem = menu.findItem(R.id.action_search);
        mSearchItem.setVisible(false);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mUserListFragment.filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}