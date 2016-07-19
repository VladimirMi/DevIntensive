//package com.softdesign.devintensive.ui.activities;
//
//import android.Manifest;
//import android.app.Dialog;
//import android.app.LoaderManager;
//import android.content.ContentValues;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.Loader;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.NavigationView;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.softdesign.devintensive.R;
//import com.softdesign.devintensive.data.network.res.UploadImageRes;
//import com.softdesign.devintensive.data.storage.models.Repository;
//import com.softdesign.devintensive.data.storage.models.UserProfile;
//import com.softdesign.devintensive.ui.loaders.UserProfileLoader;
//import com.softdesign.devintensive.ui.loaders.UserRepositoryLoader;
//import com.softdesign.devintensive.ui.views.RepositoryDeviderView;
//import com.softdesign.devintensive.ui.views.RepositoryView;
//import com.softdesign.devintensive.utils.AppConfig;
//import com.softdesign.devintensive.utils.CircleTransformation;
//import com.softdesign.devintensive.utils.ConstantManager;
//import com.softdesign.devintensive.utils.ExStorageState;
//import com.softdesign.devintensive.utils.MyTextWatcher;
//import com.squareup.picasso.Picasso;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class MainActivity extends BaseActivity
//        implements View.OnClickListener, View.OnFocusChangeListener, LoaderManager.LoaderCallbacks {
//
//    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
//    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
//    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
//    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
//    @BindView(R.id.toolbar) Toolbar mToolbar;
//    @BindView(R.id.fab) FloatingActionButton mFab;
//    @BindView(R.id.profile_placeholder) RelativeLayout mProfilePlaceholder;
//    @BindView(R.id.profile_photo) ImageView mProfilePhoto;
//    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;
//
//
//    @BindView(R.id.repositories_list) LinearLayout mRepoListView;
//
//    @BindView(R.id.phone_et) EditText mUserPhone;
//    @BindView(R.id.email_et) EditText mUserEmail;
//    @BindView(R.id.vk_et) EditText mUserVk;
//    @BindView(R.id.bio_et) EditText mUserBio;
//
//    @BindView(R.id.make_call_img) ImageView mPhoneIcon;
//    @BindView(R.id.send_email_img) ImageView mEmailIcon;
//    @BindView(R.id.vk_img) ImageView mVkIcon;
//
//    List<EditText> mUserInfoViews;
//
//    List<ImageView> mActionIcons;
//
//    @BindView(R.id.rating_txt) TextView mUserRating;
//    @BindView(R.id.code_lines_txt) TextView mUserCodeLines;
//    @BindView(R.id.projects_txt) TextView mUserProjects;
//
//
//    private boolean mEditMode;
//    private File mPhotoFile = null;
//    private AppBarLayout.LayoutParams mAppBarParams = null;
//    private Uri mOriginPhotoUri;
//
//    private UserProfile mUserProfile;
//    private List<Repository> mRepositories;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        if (savedInstanceState != null) {
//            mEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
//        }
//
//
//        getLoaderManager().initLoader(ConstantManager.REPOSITORY_LOADER, null, this).forceLoad();
//        getLoaderManager().initLoader(ConstantManager.USER_PROFILE_LOADER, null, this);
//
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            mNavigationDrawer.openDrawer(GravityCompat.START);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(TAG, "onStart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(TAG, "onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "onDestroy");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d(TAG, "onRestart");
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.make_call_img:
//                makeActionView(Uri.parse(ConstantManager.TELEPHONE_SCHEME + mUserInfoViews.get(0).getText()));
//                break;
//            case R.id.send_email_img:
//                sendEmail(Uri.parse(ConstantManager.MAIL_SCHEME + mUserInfoViews.get(1).getText()));
//                break;
//            case R.id.vk_img:
//                makeActionView(Uri.parse(ConstantManager.HTTPS_SCHEME + mUserInfoViews.get(2).getText()));
//                break;
//            case R.id.profile_placeholder:
//                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
//                break;
//            case R.id.fab:
//                if (mEditMode) {
//                    for (EditText userInfoView : mUserInfoViews) {
//                        if (!MyTextWatcher.isValid(userInfoView)) {
//                            requestFocus(userInfoView);
//                        }
//                    }
//                    if (mPhotoFile != null) {
//                        Uri editorPhotoUri = Uri.fromFile(mPhotoFile);
//                        if (!mOriginPhotoUri.equals(editorPhotoUri)) {
//                            uploadPhoto();
//                        }
//                    }
//                    mEditMode = !mEditMode;
//                    setupEditMode();
//                } else {
//                    mOriginPhotoUri = mPreferencesManager.loadUserPhoto();
//                    mEditMode = !mEditMode;
//                    setupEditMode();
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        switch (v.getId()) {
//            case R.id.phone_et:
//                break;
//            case R.id.email_et:
//                break;
//            // отсекает лишние символы перед "vk"
//            case R.id.vk_et:
//                String value = mUserInfoViews.get(2).getText().toString();
//                int startIndex = value.indexOf("vk");
//                if (startIndex > 0) {
//                    mUserInfoViews.get(2).setText(value.substring(startIndex));
//                }
//                break;
//            // отсекает лишние символы перед "github"
//            case R.id.github_et:
//                value = mUserInfoViews.get(3).getText().toString();
//                startIndex = value.indexOf("github");
//                if (startIndex > 0) {
//                    mUserInfoViews.get(3).setText(value.substring(startIndex));
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mEditMode);
//        // TODO: 7/17/2016 ?????
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
//            mNavigationDrawer.closeDrawer(GravityCompat.START);
//        } else if (mEditMode) {
//            mFab.performClick();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case ConstantManager.GALLERY_PERMISSION_REQUEST_CODE:
//                for (int grantResult : grantResults) {
//                    if (grantResult == PackageManager.PERMISSION_DENIED) {
//                        showPermissionSnackbar();
//                        return;
//                    }
//                }
//                loadPhotoFromGallery();
//                break;
//            case ConstantManager.CAMERA_PERMISSION_REQUEST_CODE:
//                for (int grantResult : grantResults) {
//                    if (grantResult == PackageManager.PERMISSION_DENIED) {
//                        showPermissionSnackbar();
//                        return;
//                    }
//                }
//                loadPhotoFromCamera();
//                break;
//        }
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case ConstantManager.LOAD_PROFILE_PHOTO:
//                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
//                        getString(R.string.user_profile_diallog_camera),
//                        getString(R.string.user_profile_dialog_cancel)};
//
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(R.string.user_profile_dialog_title);
//                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int choiceItem) {
//                        switch (choiceItem) {
//                            case 0:
//                                loadPhotoFromGallery();
//                                break;
//                            case 1:
//                                loadPhotoFromCamera();
//                                break;
//                            case 2:
//                                dialogInterface.cancel();
//                                break;
//                        }
//                    }
//                });
//                return builder.create();
//            default:
//                return null;
//        }
//    }
//
//
//    private void showSnackBar(String message) {
//        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
//    }
//
//
//    private void loadUserPhoto() {
//        Picasso.with(this)
//                .load(mUserProfile.getPhoto())
//                .placeholder(R.color.grey_light)
//                .into(mProfilePhoto);
//    }
//
//
//    private void loadPhotoFromGallery() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_GRANTED) {
//
//            Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            takeGalleryIntent.setType(ConstantManager.MIME_TYPE_IMAGE);
//            startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.chooser_gallery)),
//                    ConstantManager.REQUEST_GALLERY_PICTURE);
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    ConstantManager.GALLERY_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    private void loadPhotoFromCamera() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
//                PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
//                        PackageManager.PERMISSION_GRANTED) {
//
//            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//            try {
//                mPhotoFile = createImageFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (mPhotoFile != null) {
//                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
//                startActivityForResult(Intent.createChooser(takeCaptureIntent, getString(R.string.chooser_camera)),
//                        ConstantManager.REQUEST_CAMERA_PICTURE);
//            } else {
//                showSnackBar(getString(R.string.err_msg_ext_storage));
//            }
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, ConstantManager.CAMERA_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    /**
//     * Получение рузультата из другой Activity (фото из галлереи или камеры)
//     *
//     * @param requestCode The integer request code originally supplied to startActivityForResult(),
//     *                    allowing you to identify who this result came from.
//     * @param resultCode  The integer result code returned by the child activity through its setResult().
//     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Uri selectedImage;
//        switch (requestCode) {
//            case ConstantManager.REQUEST_CAMERA_PICTURE:
//                if (mPhotoFile != null && resultCode == RESULT_OK) {
//                    selectedImage = Uri.fromFile(mPhotoFile);
//                    insertProfileImage(selectedImage);
//                }
//                break;
//            case ConstantManager.REQUEST_GALLERY_PICTURE:
//                if (data != null && resultCode == RESULT_OK) {
//                    selectedImage = data.getData();
//                    mPhotoFile = new File(selectedImage.getPath());
//                    insertProfileImage(selectedImage);
//                }
//                break;
//        }
//    }
//
//    private void hideProfilePlaceholder() {
//        mProfilePlaceholder.setVisibility(View.INVISIBLE);
//    }
//
//    private void showProfilePlaceholder() {
//        mProfilePlaceholder.setVisibility(View.VISIBLE);
//    }
//
//    private void lockToolbar() {
//        mAppBarLayout.setExpanded(true, true);
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mAppBarParams.setScrollFlags(0);
//                mCollapsingToolbar.setLayoutParams(mAppBarParams);
//            }
//        }, 500);
//    }
//
//    private void unlockToolbar() {
//        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
//                AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
//        mCollapsingToolbar.setLayoutParams(mAppBarParams);
//    }
//
//    private void showPermissionSnackbar() {
//        Snackbar.make(mCoordinatorLayout, getString(R.string.permission_snackbar_text), Snackbar.LENGTH_LONG)
//                .setAction(getString(R.string.permission_snackbar_title), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        openApplicationSettings();
//                    }
//                }).show();
//    }
//
//    private void openApplicationSettings() {
//        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                Uri.parse(ConstantManager.PACKAGE_SCHEME + getPackageName()));
//        startActivityForResult(appSettingsIntent, ConstantManager.SETTINGS_PERMISSION_REQUEST_CODE);
//    }
//
//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat(AppConfig.TIMESTAMP_FORMAT,
//                getResources().getConfiguration().locale).format(new Date());
//        String imageFileName = AppConfig.PHOTO_FILE_PREFIX + timeStamp;
//        File imageFile;
//        if (ExStorageState.isWritable()) {
//            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            imageFile = File.createTempFile(imageFileName, ConstantManager.EXTENSION_JPEG, storageDir);
//        } else {
//            return null;
//        }
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
//        values.put(MediaStore.Images.Media.MIME_TYPE, ConstantManager.MIME_TYPE_JPEG);
//        values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());
//
//        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//        return imageFile;
//    }
//
//    /**
//     * Установка фотографии профайла
//     *
//     * @param selectedImage URI выбранного изображения
//     */
//    private void insertProfileImage(Uri selectedImage) {
//        Picasso.with(this)
//                .load(selectedImage)
//                .placeholder(R.color.grey_light)
//                .into(mProfilePhoto);
//        mPreferencesManager.saveUserPhoto(selectedImage);
//    }
//
//    /**
//     * Создание и запуск SENDTO интента
//     *
//     * @param uri цель SENDTO
//     */
//    private void sendEmail(Uri uri) {
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
//        startActivity(Intent.createChooser(emailIntent, getString(R.string.chooser_email)));
//    }
//
//    /**
//     * Создание и запуск ACTION_VIEW интента
//     *
//     * @param uri цель ACTION_VIEW
//     */
//    private void makeActionView(Uri uri) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
//    }
//
//    /**
//     * Запрос фокуса у EditText и если возможно установка его и перемещение курсора
//     * в конец строки
//     *
//     * @param editText EditText у которого запрашивается фокус
//     */
//    public void requestFocus(EditText editText) {
//        if (editText.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//            editText.setSelection(editText.length());
//        }
//    }
//
//    private void uploadPhoto() {
//        RequestBody requestFile = RequestBody.create(
//                MediaType.parse(ConstantManager.CONTENT_TYPE_MULTIPART), mPhotoFile);
//        MultipartBody.Part body = MultipartBody.Part.createFormData(
//                AppConfig.PHOTO_FORM_KEY, mPhotoFile.getName(), requestFile);
//
//        Call<UploadImageRes> call = mDataManager.uploadPhoto(mPreferencesManager.getUserId(), body);
//        call.enqueue(new Callback<UploadImageRes>() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                Log.d(TAG, "uploadPhoto onResponse");
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                Log.d(TAG, "uploadPhoto onFailure");
//            }
//        });
//    }
//
//    private void initUI() {
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//        hideProgress();
//
//        mUserPhone.setText(mUserProfile.getPhone());
//        mUserEmail.setText(mUserProfile.getEmail());
//        mUserVk.setText(mUserProfile.getVk());
//        mUserBio.setText(mUserProfile.getBio());
//        mUserRating.setText(String.valueOf(mUserProfile.getRating()));
//        mUserCodeLines.setText(String.valueOf(mUserProfile.getCodeLines()));
//        mUserProjects.setText(String.valueOf(mUserProfile.getProjects()));
//
//        mUserInfoViews = new ArrayList<>();
//        mUserInfoViews.add(mUserPhone);
//        mUserInfoViews.add(mUserEmail);
//        mUserInfoViews.add(mUserVk);
//
//        mActionIcons = new ArrayList<>();
//        mActionIcons.add(mPhoneIcon);
//        mActionIcons.add(mEmailIcon);
//        mActionIcons.add(mVkIcon);
//
//        initRepositoriesView();
//
//        mUserInfoViews.add(mUserBio);
//
//        setupToolbar();
//        setupDrawer();
//        setupEditMode();
//
//        mFab.setOnClickListener(this);
//        mProfilePlaceholder.setOnClickListener(this);
//
//        for (View actionIcon : mActionIcons) {
//            actionIcon.setOnClickListener(this);
//        }
//        //последним в списке bio, у которого нет actionIcon
//        for (int i = 0; i < mUserInfoViews.size()-1; i++) {
//            mUserInfoViews.get(i).setOnFocusChangeListener(this);
//            mUserInfoViews.get(i).addTextChangedListener(new MyTextWatcher(this,
//                    mUserInfoViews.get(i), mActionIcons.get(i)));
//        }
//
//        loadUserPhoto();
//    }
//
//    private void initRepositoriesView() {
//
//        for (int i = 0; i < mRepositories.size(); i++) {
//            RepositoryView repositoryView = new RepositoryView(this, mRepositories.get(i).getRepositoryName()
//                    , new RepositoryView.CustomClickListener() {
//                @Override
//                public void onIconClickListener(Uri uri) {
//                    makeActionView(uri);
//                }
//            });
//            mRepoListView.addView(repositoryView);
//
//            if (i < mRepositories.size() - 1) {
//                RepositoryDeviderView deviderView = new RepositoryDeviderView(this);
//                mRepoListView.addView(deviderView);
//            }
//            mUserInfoViews.add(repositoryView.getGitEditText());
//            mActionIcons.add(repositoryView.getGitImage());
//        }
//    }
//
//    private void setupToolbar() {
//        setSupportActionBar(mToolbar);
//        ActionBar actionBar = getSupportActionBar();
//
//        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
//        if (actionBar != null) {
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        mCollapsingToolbar.setTitle(mUserProfile.getFullName());
//    }
//
//
//    private void setupDrawer() {
//        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
//        assert navigationView != null;
//        View header = navigationView.getHeaderView(0);
//
//        // установка круглого аватара
//        ImageView userAvatar = (ImageView) header.findViewById(R.id.user_avatar);
//        Picasso.with(this)
//                .load(mUserProfile.getAvatar())
//                .placeholder(R.color.grey_light)
//                .transform(new CircleTransformation())
//                .into(userAvatar);
//
//        TextView userName = (TextView) header.findViewById(R.id.user_name_txt);
//        TextView userEmail = (TextView) header.findViewById(R.id.user_email_txt);
//        userName.setText(mUserProfile.getFullName());
//        userEmail.setText(mUserProfile.getEmail());
//
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                item.setChecked(true);
//                mNavigationDrawer.closeDrawer(GravityCompat.START);
//                int itemId = item.getItemId();
//                switch (itemId) {
//                    case R.id.login_menu:
//                        mPreferencesManager.saveUserId("");
//                        mPreferencesManager.saveAuthToken("");
//                        Intent loginIntent = new Intent(MainActivity.this, AuthActivity.class);
//                        startActivity(loginIntent);
//                        finish();
//                        return true;
//                    case R.id.users_menu:
//                        Intent usersIntent = new Intent(MainActivity.this, UserListActivity.class);
//                        startActivity(usersIntent);
//                        return true;
//                }
//                return false;
//            }
//        });
//    }
//
//    /**
//     * переключает режим редактирования
//     */
//    private void setupEditMode() {
//        if (mEditMode) {
//            for (EditText userValue : mUserInfoViews) {
//                userValue.setFocusableInTouchMode(true);
//                userValue.setFocusable(true);
//                userValue.setEnabled(true);
//            }
//            showProfilePlaceholder();
//            lockToolbar();
//            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
//            mFab.setImageResource(R.drawable.ic_check_24dp);
//            requestFocus(mUserInfoViews.get(0));
//        } else {
//            for (EditText userValue : mUserInfoViews) {
//                userValue.setFocusable(false);
//                userValue.setEnabled(false);
//            }
//            hideProfilePlaceholder();
//            unlockToolbar();
//            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white));
//            mFab.setImageResource(R.drawable.ic_mode_edit_24dp);
//        }
//    }
//
//
//    @Override
//    public Loader onCreateLoader(int id, Bundle args) {
//        Loader loader = null;
//        switch (id) {
//            case ConstantManager.USER_PROFILE_LOADER:
//                loader = new UserProfileLoader(this, args);
//                break;
//            case ConstantManager.REPOSITORY_LOADER:
//                Bundle bundle = new Bundle();
//                bundle.putString(ConstantManager.USER_ID_KEY, mPreferencesManager.getUserId());
//                loader = new UserRepositoryLoader(this, bundle);
//                break;
//        }
//        return loader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader loader, Object data) {
//        switch (loader.getId()) {
//            case ConstantManager.USER_PROFILE_LOADER:
//                mUserProfile = (UserProfile) data;
//                initUI();
//                break;
//            case ConstantManager.REPOSITORY_LOADER:
//                mRepositories = (List<Repository>) data;
//                getLoaderManager().getLoader(ConstantManager.USER_PROFILE_LOADER).forceLoad();
//                break;
//        }
//    }
//
//    @Override
//    public void onLoaderReset(Loader loader) {
//    }
//}
