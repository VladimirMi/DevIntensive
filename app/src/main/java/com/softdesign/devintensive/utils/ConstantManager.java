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

    // Misc constants
    String TAG_PREFIX = "DEV ";
    String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    String EXTENSION_JPEG = ".jpg";
    String PHOTO_FILE_PREFIX = "JPEG_";

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

    // Validation patterns
    Pattern EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{3,256}" +
                    "\\@[a-zA-Z0-9]{2,64}" +
                    "\\.[a-zA-Z0-9]{2,25}"
    );

    Pattern PHONE = Pattern.compile(
            "\\+?+[0-9]{1,3}[\\- \\.]?[0-9]{0,3}[\\- \\.]?"
                    + "\\(?[0-9]{3,5}\\)?[\\- \\.]?"
                    + "([0-9]{3}[\\- \\.]?[0-9]{2,3})[\\- \\.]?[0-9]{2,3}");

    Pattern VK_URL = Pattern.compile(
            "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/|)"
                    + "(?:vk\\.com|new\\.vk\\.com)"
                    + "(\\/(?:(?:[" + Patterns.GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~"
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_]){3,}|(?:\\%[a-fA-F0-9]{3,}))*)+"
                    + "(?:\\b|$))");
    Pattern GIT_URL = Pattern.compile(
            "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/|)"
                    + "(?:github\\.com)"
                    + "(\\/(?:(?:[" + Patterns.GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~"
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_]){3,}|(?:\\%[a-fA-F0-9]{3,}))*)?"
                    + "(?:\\b|$))");
}
