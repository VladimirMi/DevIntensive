package com.softdesign.devintensive.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public interface ConstantManager {
    // Preferences Keys
    String USER_PHONE_KEY = "USER_PHONE_KEY";
    String USER_MAIL_KEY = "USER_MAIL_KEY";
    String USER_VK_KEY = "USER_VK_KEY";
    String USER_GIT_KEY = "USER_GIT_KEY";
    String USER_ABOUT_KEY = "USER_ABOUT_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";
    String USER_AVATAR_KEY = "USER_AVATAR_KEY";
    String USER_RATING_KEY = "USER_RATING_KEY";
    String USER_CODE_LINES_KEY = "USER_CODE_LINES_KEY";
    String USER_PROJECTS_KEY = "USER_PROJECTS_KEY";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";
    String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    String USER_ID_KEY = "USER_ID_KEY";
    String USER_NAME_KEY = "USER_NAME_KEY";

    // Misc constants
    String TAG_PREFIX = "DEV ";
    String EXTENSION_JPEG = ".jpg";

    // Dialog ids
    int LOAD_PROFILE_PHOTO = 1;

    // Intent request codes
    int REQUEST_CAMERA_PICTURE = 50;
    int REQUEST_GALLERY_PICTURE = 51;

    // Permission request codes
    int SETTINGS_PERMISSION_REQUEST_CODE = 100;
    int CAMERA_PERMISSION_REQUEST_CODE = 101;
    int GALLERY_PERMISSION_REQUEST_CODE = 102;

    // URI schemes
    String TELEPHONE_SCHEME = "tel:";
    String MAIL_SCHEME = "mailto:";
    String HTTPS_SCHEME = "https:";
    String PACKAGE_SCHEME = "package:";

    // Mime types
    String MIME_TYPE_IMAGE = "image/*";
    String MIME_TYPE_JPEG = "image/jpeg";

    // Content types
    String CONTENT_TYPE_MULTIPART = "multipart/form-data";
}
