package com.softdesign.devintensive.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public interface AppConfig {
    // URL
    String BASE_URL = "http://devintensive.softdesign-apps.ru/api/";
    String FORGOT_PASS_URL = "http://devintensive.softdesign-apps.ru/forgotpass";

    // Validation patterns
    Pattern EMAIL_ADDRESS_VALIDATE = Pattern.compile(
            "[a-zA-Z0-9\\+\\._%\\-]{3,256}" +
                    "@[a-zA-Z0-9]{2,64}" +
                    "\\.[a-zA-Z0-9]{2,25}"
    );

    Pattern PHONE_VALIDATE = Pattern.compile(
            "\\+?+[0-9]{1,3}[\\- \\.]?[0-9]{0,3}[\\- \\.]?"
                    + "\\(?[0-9]{3,5}\\)?[\\- \\.]?"
                    + "([0-9]{3}[\\- \\.]?[0-9]{2,3})[\\- \\.]?[0-9]{2,3}");

    Pattern VK_URL_VALIDATE = Pattern.compile(
            "((?:(http|https|Http|Https|rtsp|Rtsp)://|)"
                    + "(?:vk\\.com|new\\.vk\\.com)"
                    + "(/(?:(?:[" + Patterns.GOOD_IRI_CHAR + ";/\\?:@&=#~"
                    + "\\-\\.\\+!\\*'\\(\\),_]){3,}|(?:%[a-fA-F0-9]{3,}))*)+"
                    + "(?:\\b|$))");

    Pattern GIT_URL_VALIDATE = Pattern.compile(
            "((?:(http|https|Http|Https|rtsp|Rtsp)://|)"
                    + "(?:github\\.com)"
                    + "(/(?:(?:[" + Patterns.GOOD_IRI_CHAR + ";/\\?:@&=#~"
                    + "\\-\\.\\+!\\*'\\(\\),_]){3,}|(?:%[a-fA-F0-9]{3,}))*)?"
                    + "(?:\\b|$))");

    // Form keys
    String PHOTO_FORM_KEY = "photo";

    // Misc configs
    String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    String PHOTO_FILE_PREFIX = "JPEG_";

    // Time configs
    long MAX_CONNECT_TIMEOUT = 15_000;
    long MAX_READ_TIMEOUT = 15_000;
    long SEARCH_DELAY = 1000;
}
