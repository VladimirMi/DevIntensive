package com.softdesign.devintensive.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public interface ConstantManager {
    String TAG_PREFIX = "DEV ";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY = "USER_KEY_1";
    String USER_MAIL_KEY = "USER_KEY_2";
    String USER_VK_KEY = "USER_KEY_3";
    String USER_GIT_KEY = "USER_KEY_4";
    String USER_ABOUT_KEY = "USER_KEY_5";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";

    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE = 101;
    int REQUEST_GALLERY_PICTURE = 102;
    int SETTINGS_PERMISSION_REQUEST_CODE = 103;
    int CAMERA_PERMISSION_REQUEST_CODE = 104;


    Pattern EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{3,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" +
                    ")+"
    );

    Pattern PHONE = Pattern.compile(
            "\\+?+[0-9]{1,3}[\\- \\.]?[0-9]{0,3}[\\- \\.]?"
                    + "\\(?[0-9]{3,5}\\)?[\\- \\.]?"
                    + "([0-9]{3}[\\- \\.]?[0-9]{2,3})[\\- \\.]?[0-9]{2,3}");


    Pattern VK_URL = Pattern.compile(
            "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/|)"
                    + "(?:vk\\.com|new\\.vk\\.com)"
                    + "(\\/(?:(?:[" + Patterns.GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~"
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
                    + "(?:\\b|$))");

    Pattern GIT_URL = Pattern.compile(
            "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/|)"
                    + "(?:github\\.com)"
                    + "(\\/(?:(?:[" + Patterns.GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~"
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
                    + "(?:\\b|$))");
}
