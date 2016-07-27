package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UploadImageRes;
import com.softdesign.devintensive.ui.views.RepositoryDeviderView;
import com.softdesign.devintensive.ui.views.RepositoryView;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ExStorageState;
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

public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.profile_placeholder) RelativeLayout mProfilePlaceholder;
    @BindView(R.id.profile_photo) ImageView mProfilePhoto;
    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.nested_scroll) NestedScrollView mNestedScrollView;

    @BindView(R.id.repositories_list) LinearLayout mRepoListView;

    @BindView(R.id.phone_et) EditText mUserPhone;
    @BindView(R.id.email_et) EditText mUserEmail;
    @BindView(R.id.vk_et) EditText mUserVk;
    @BindView(R.id.bio_et) EditText mUserBio;

    @BindView(R.id.make_call_img) ImageView mPhoneIcon;
    @BindView(R.id.send_email_img) ImageView mEmailIcon;
    @BindView(R.id.vk_img) ImageView mVkIcon;

    List<EditText> mUserInfoViews;

    List<ImageView> mActionIcons;

    @BindViews({R.id.rating_txt, R.id.code_lines_txt, R.id.projects_txt})
    TextView[] mUserStatisticViews;

    private boolean mEditMode;
    private File mPhotoFile = null;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private Uri mOriginPhotoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        ButterKnife.bind(this);

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

        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        for (int i = 0; i < mUserInfoViews.size() - 1; i++) {
            mUserInfoViews.get(i).addTextChangedListener(new MyTextWatcher(this,
                    mUserInfoViews.get(i), mActionIcons.get(i)));
        }

        if (savedInstanceState != null) {
            mEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
        }

        setupToolbar();
        setupDrawer();
        setupEditMode();

        loadUserInfo();
        loadUserStatistic();
        loadUserPhoto();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
        mNavigationView.getMenu().getItem(0).setChecked(true);
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
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.fab:
                if (mEditMode) {
                    for (EditText userInfoView : mUserInfoViews) {
                        if (!MyTextWatcher.isValid(userInfoView)) {
                            requestFocus(userInfoView);
                        }
                    }
                    if (mPhotoFile != null) {
                        Uri editorPhotoUri = Uri.fromFile(mPhotoFile);
                        if (!mOriginPhotoUri.equals(editorPhotoUri)) {
                            uploadPhoto();
                        }
                    }
                    mEditMode = !mEditMode;
                    setupEditMode();
                } else {
                    mOriginPhotoUri = Uri.parse(mPreferencesManager.loadUserPhoto());
                    mEditMode = !mEditMode;
                    setupEditMode();
                }
                break;
        }
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mEditMode);
        saveUserInfo();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loadUserInfo();
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
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mPreferencesManager.loadUserName());
        }
    }

    private void setupDrawer() {
        mNavigationView.getMenu().getItem(0).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.login_menu:
                        mPreferencesManager.clearAllData();
                        mPreferencesManager.saveAuthToken(ConstantManager.INVALID_TOKEN);
                        Intent loginIntent = new Intent(MainActivity.this, AuthActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                        finish();
                        return true;
                    case R.id.users_menu:
                        Intent usersIntent = new Intent(MainActivity.this, UserListActivity.class);
                        startActivity(usersIntent);
                        return true;
                }
                return false;
            }
        });

        // установка круглого аватара
        final ImageView userAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        UiHelper.setUserAvatar(this, mPreferencesManager.loadUserAvatar(), userAvatar);

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
            for (EditText userValue : mUserInfoViews) {
                userValue.setFocusableInTouchMode(true);
                userValue.setFocusable(true);
                userValue.setEnabled(true);
            }
            for (ImageView actionIcon : mActionIcons) {
                actionIcon.setClickable(false);
            }
            showProfilePlaceholder();
//            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            mFab.setImageResource(R.drawable.ic_check_24dp);
            requestFocus(mUserInfoViews.get(0));
        } else {
            for (EditText userValue : mUserInfoViews) {
                userValue.setFocusable(false);
                userValue.setEnabled(false);
            }
            for (ImageView actionIcon : mActionIcons) {
                actionIcon.setClickable(true);
            }
            hideProfilePlaceholder();
//            unlockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white));
            mFab.setImageResource(R.drawable.ic_mode_edit_24dp);
            mNestedScrollView.scrollTo(0, 0);
        }

    }

    private void initRepositoriesView() {

        for (int i = 0; i < mPreferencesManager.getRepositoriesSize(); i++) {
            RepositoryView repositoryView = new RepositoryView(this, new RepositoryView.CustomClickListener() {
                @Override
                public void onIconClickListener(Uri uri) {
                    makeActionView(uri);
                }
            });
            mRepoListView.addView(repositoryView);

            if (i < mPreferencesManager.getRepositoriesSize() - 1) {
                RepositoryDeviderView deviderView = new RepositoryDeviderView(this);
                mRepoListView.addView(deviderView);
            }
            mUserInfoViews.add(repositoryView.getGitEditText());
            mActionIcons.add(repositoryView.getGitImage());
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

    private void loadUserPhoto() {
        UiHelper.setUserPhoto(this, mPreferencesManager.loadUserPhoto(), mProfilePhoto);
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
                mPhotoFile = createImageFile();
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

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.INVISIBLE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        //убирает конфликт с анимацией
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAppBarParams.setScrollFlags(0);
            }
        }, 500);

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

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(AppConfig.TIMESTAMP_FORMAT,
                getResources().getConfiguration().locale).format(new Date());
        String imageFileName = AppConfig.PHOTO_FILE_PREFIX + timeStamp;
        File imageFile;
        if (ExStorageState.isWritable()) {
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            imageFile = File.createTempFile(imageFileName, ConstantManager.EXTENSION_JPEG, storageDir);
        } else {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, ConstantManager.MIME_TYPE_JPEG);
        values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return imageFile;
    }

    /**
     * Создание и запуск SENDTO интента
     *
     * @param uri цель SENDTO
     */
    private void sendEmail(Uri uri) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(emailIntent, getString(R.string.chooser_email)));
    }

    /**
     * Создание и запуск ACTION_VIEW интента
     *
     * @param uri цель ACTION_VIEW
     */
    private void makeActionView(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * Запрос фокуса у EditText и если возможно установка его и перемещение курсора
     * в конец строки
     *
     * @param editText EditText у которого запрашивается фокус
     */
    public void requestFocus(EditText editText) {
        if (editText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            editText.setSelection(editText.length());
        }
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
}