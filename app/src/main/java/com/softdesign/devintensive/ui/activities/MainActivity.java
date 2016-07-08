package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ExStorageState;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    @BindView(R.id.profile_placeholder) RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.profile_photo) ImageView mProfilePhoto;
    private int mCurrentEditMode;

    @BindViews({R.id.phone_et, R.id.email_et, R.id.vk_et, R.id.github_et, R.id.about_me_et})
    EditText[] mUserInfoViews;

    @BindViews({R.id.phone_til, R.id.email_til, R.id.vk_til, R.id.github_til, R.id.about_me_til})
    TextInputLayout[] mUserInfoInputLayouts;

    @BindViews({R.id.make_call_img, R.id.send_email_img, R.id.vk_img, R.id.github_img})
    ImageView[] mActionIcons;

    private DataManager mDataManager;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;

    private AppBarLayout.LayoutParams mAppBarParams = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        ButterKnife.bind(this);


        mDataManager = DataManager.getInstance();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);


        assert mFab != null;
        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        for (View actionIcon : mActionIcons) {
            actionIcon.setOnClickListener(this);
        }

        for (EditText userInfoView : mUserInfoViews) {
            userInfoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    switch (v.getId()) {
                        case R.id.phone_et:
                            break;
                        case R.id.email_et:
                            break;
                        case R.id.vk_et:
                            String value = mUserInfoViews[2].getText().toString();
                            int startIndex = value.indexOf("vk");
                            if (startIndex > 0) {
                                mUserInfoViews[2].setText(value.substring(startIndex));
                            }
                            break;
                        case R.id.github_et:
                            value = mUserInfoViews[3].getText().toString();
                            startIndex = value.indexOf("github");
                            if (startIndex > 0) {
                                mUserInfoViews[3].setText(value.substring(startIndex));
                            }
                            break;
                    }
                }
            });
            userInfoView.addTextChangedListener(new MyTextWatcher(userInfoView));
        }


        setupToolbar();
        setupDrawer();

        // TODO: 7/5/2016 placeeholder + transform + crop(512*256)
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .into(mProfilePhoto);


        if (savedInstanceState != null) {
            // recreating activity
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            loadUserInfoValue();
        } else {
            // first starting of the activity
            saveUserInfoValue();
        }
        changeEditorMode(mCurrentEditMode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.make_call_img:
                makeActionView(Uri.parse("tel:" + mUserInfoViews[0].getText()));
                break;
            case R.id.send_email_img:
                sendEmail(Uri.parse("mailto:" + mUserInfoViews[1].getText()));
                break;
            case R.id.vk_img:
                makeActionView(Uri.parse("https:" + mUserInfoViews[2].getText()));
                break;
            case R.id.github_img:
                makeActionView(Uri.parse("https:" + mUserInfoViews[3].getText()));
                break;
            case R.id.fab:
                if (mCurrentEditMode == 0) {
                    mCurrentEditMode = 1;
                } else {
                    mCurrentEditMode = 0;
                }
                changeEditorMode(mCurrentEditMode);
                break;
            case R.id.profile_placeholder:
                // TODO: 7/5/2016 сделать выбор откуда загружать фото
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
        saveUserInfoValue();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Полученте рузультата из другой Activity (фото из галлереи или камеры)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (mPhotoFile != null && resultCode == RESULT_OK) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (data != null && resultCode == RESULT_OK) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_PERMISSION_REQUEST_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 7/5/2016 обработать разрешение
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 7/5/2016 обработать разрешение
            }
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.login_menu:
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        return true;
                }
                return false;
            }
        });
        // установка круглого аватара
        ImageView userAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar);
        RoundedAvatarDrawable roundedAvatar = new RoundedAvatarDrawable(avatar);
        userAvatar.setImageDrawable(roundedAvatar);

    }

    /**
     * переключает режим редактирования
     *
     * @param mode если 1 - режим редактирования, если 0 - режим просмотра
     */
    private void changeEditorMode(int mode) {
        if (mode == 1) {
            for (EditText userValue : mUserInfoViews) {
                userValue.setFocusable(true);
                userValue.setEnabled(true);
                userValue.setFocusableInTouchMode(true);
                showProfilePlaceholder();
                lockToolbar();
                requestFocus(mUserInfoViews[0]);
            }
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            mFab.setImageResource(R.drawable.ic_check_black_24dp);
        } else {
            if (!validatePhone()) return;
            if (!validateEmail()) return;
            if (!validateVk()) return;
            if (!validateGit()) return;
            for (EditText userValue : mUserInfoViews) {
                userValue.setFocusable(false);
                userValue.setEnabled(false);
                userValue.setFocusableInTouchMode(false);
                hideProfilePlaceholder();
                unlockToolbar();
            }
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
            mFab.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        }

    }

    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews[i].setText(userData.get(i));
        }
    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText userInfoView : mUserInfoViews) {
            userData.add(userInfoView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, "выберите фото"),
                ConstantManager.REQUEST_GALLERY_PICTURE);
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
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            } else {
                showSnackBar("Внешняя память не доступна");
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_PERMISSION_REQUEST_CODE);

            Snackbar.make(mCoordinatorLayout, "Для корректной работы приложения необходимо дать требуемые разрешения", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
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
                                // TODO: 7/5/2016 load from gallery
                                loadPhotoFromGallery();
//                                showSnackBar("load from gallery");
                                break;
                            case 1:
                                // TODO: 7/5/2016 load from camera
                                loadPhotoFromCamera();
//                                showSnackBar("load from camera");
                                break;
                            case 2:
                                // TODO: 7/5/2016 cancel
                                dialogInterface.cancel();
//                                showSnackBar("cancel");
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File imageFile = null;
        if (ExStorageState.isWritable()) {
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return imageFile;
    }

    private void insertProfileImage(Uri selectedImage) {
        // TODO: 7/5/2016 placeholder + transform + crop(512*256)
        Picasso.with(this)
                .load(selectedImage)
                .error(R.drawable.ic_warning_black_24dp)
                .into(mProfilePhoto);

        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.SETTINGS_PERMISSION_REQUEST_CODE);
    }


    private void sendEmail(Uri uri) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

    private void makeActionView(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(intent, "Make action view"));
    }

    public boolean validatePhone() {
        String phone = mUserInfoViews[0].getText().toString().trim();
        Boolean isValidPhone = !TextUtils.isEmpty(phone) &&
                ConstantManager.PHONE.matcher(phone).matches();

        if (isValidPhone) {
            mUserInfoInputLayouts[0].setHint(getString(R.string.phone_hint));
            mActionIcons[0].setClickable(true);
            mActionIcons[0].setImageResource(R.drawable.ic_call_black_24dp);
            return true;
        } else {
            mUserInfoInputLayouts[0].setHint(getString(R.string.err_msg_email));
            mActionIcons[0].setClickable(false);
            mActionIcons[0].setImageResource(R.drawable.ic_error_grey_24dp);
            requestFocus(mUserInfoViews[0]);
            return false;
        }
    }

    private boolean validateEmail() {
        String email = mUserInfoViews[1].getText().toString().trim();
        Boolean isValidEmail = !TextUtils.isEmpty(email) &&
                ConstantManager.EMAIL_ADDRESS.matcher(email).matches();

        if (isValidEmail) {
            mUserInfoInputLayouts[1].setHint(getString(R.string.email_hint));
            mActionIcons[1].setClickable(true);
            mActionIcons[1].setImageResource(R.drawable.ic_send_black_24dp);
            return true;
        } else {
            mUserInfoInputLayouts[1].setHint(getString(R.string.err_msg_email));
            mActionIcons[1].setClickable(false);
            mActionIcons[1].setImageResource(R.drawable.ic_error_grey_24dp);
            requestFocus(mUserInfoViews[1]);
            return false;
        }
    }

    private boolean validateVk() {
        String vk = mUserInfoViews[2].getText().toString().trim();
        Boolean isValidVk = !TextUtils.isEmpty(vk) &&
                ConstantManager.VK_URL.matcher(vk).matches();

        if (isValidVk) {
            mUserInfoInputLayouts[2].setHint(getString(R.string.vk_hint));
            mActionIcons[2].setClickable(true);
            mActionIcons[2].setImageResource(R.drawable.ic_vk_social_network_logo);
            return true;
        } else {
            mUserInfoInputLayouts[2].setHint(getString(R.string.err_msg_email));
            mActionIcons[2].setClickable(false);
            mActionIcons[2].setImageResource(R.drawable.ic_error_grey_24dp);
            requestFocus(mUserInfoViews[2]);
            return false;
        }
    }

    private boolean validateGit() {
        String git = mUserInfoViews[3].getText().toString().trim();
        Boolean isValidGit = !TextUtils.isEmpty(git) &&
                ConstantManager.GIT_URL.matcher(git).matches();

        if (isValidGit) {
            mUserInfoInputLayouts[3].setHint(getString(R.string.github_hint));
            mActionIcons[3].setClickable(true);
            mActionIcons[3].setImageResource(R.drawable.ic_github_logo);
            return true;
        } else {
            mUserInfoInputLayouts[3].setHint(getString(R.string.err_msg_email));
            mActionIcons[3].setClickable(false);
            mActionIcons[3].setImageResource(R.drawable.ic_error_grey_24dp);
            requestFocus(mUserInfoViews[3]);
            return false;
        }
    }


    private void requestFocus(EditText editText) {
        Log.d(TAG, "requestFocus: " + editText.getText() + " " + editText.requestFocus());
        if (editText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private final EditText mEditText;

        public MyTextWatcher(EditText text) {
            mEditText = text;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (mEditText.getId()) {
                case R.id.phone_et:
                    validatePhone();
                    break;
                case R.id.email_et:
                    validateEmail();
                    break;
                case R.id.vk_et:
                    validateVk();
                    break;
                case R.id.github_et:
                    validateGit();
                    break;
            }
        }
    }


}
