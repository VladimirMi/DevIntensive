package com.softdesign.devintensive.utils;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.activities.MainActivity;

public class MyTextWatcher implements TextWatcher {
    private final EditText mEditText;
    private final TextInputLayout mInputLayout;
    private final ImageView mActionIcon;
    private MainActivity mActivityReference;

    public MyTextWatcher(EditText text, MainActivity activity) {
        mEditText = text;
        mInputLayout = (TextInputLayout) mEditText.getParent();
        LinearLayout userInfoItem = (LinearLayout) mInputLayout.getParent().getParent();
        mActionIcon = (ImageView) (userInfoItem).getChildAt(2);
        mActivityReference = activity;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        String value = mEditText.getText().toString().trim();
        switch (mEditText.getId()) {
            case R.id.phone_et:
                validatePhone(value);
                break;
            case R.id.email_et:
                validateEmail(value);
                break;
            case R.id.vk_et:
                validateVk(value);
                break;
            case R.id.github_et:
                validateGit(value);
                break;
        }
    }

    private boolean validatePhone(String phone) {
        return validate(isValidPhone(phone),
                mActivityReference.getString(R.string.phone_hint),
                mActivityReference.getString(R.string.err_msg_phone),
                R.drawable.ic_call_24dp,
                R.drawable.ic_error_24dp);
    }

    private boolean validateEmail(String email) {
        return validate(isValidEmail(email),
                mActivityReference.getString(R.string.email_hint),
                mActivityReference.getString(R.string.err_msg_email),
                R.drawable.ic_send_24dp,
                R.drawable.ic_error_24dp);
    }

    private boolean validateVk(String vk) {
        return validate(isValidVk(vk),
                mActivityReference.getString(R.string.vk_hint),
                mActivityReference.getString(R.string.err_msg_vk),
                R.drawable.ic_vk_social_network_logo,
                R.drawable.ic_error_24dp);
    }

    private boolean validateGit(String git) {
        return validate(isValidGit(git),
                mActivityReference.getString(R.string.github_hint),
                mActivityReference.getString(R.string.err_msg_git),
                R.drawable.ic_github_logo,
                R.drawable.ic_error_24dp);
    }

    private boolean validate(Boolean isValid, String hint, String errMsg, int actionIcon, int errIcon) {
        if (isValid) {
            mInputLayout.setHint(hint);
            mActionIcon.setClickable(true);
            mActionIcon.setImageResource(actionIcon);
            return true;
        } else {
            mInputLayout.setHint(errMsg);
            mActionIcon.setClickable(false);
            mActionIcon.setImageResource(errIcon);
            requestFocus(mEditText);
            return false;
        }
    }

    public static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) &&
                AppConfig.PHONE_VALIDATE.matcher(phone).matches();
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) &&
                AppConfig.EMAIL_ADDRESS_VALIDATE.matcher(email).matches();
    }

    public static boolean isValidVk(String vk) {
        return !TextUtils.isEmpty(vk) &&
                AppConfig.VK_URL_VALIDATE.matcher(vk).matches();
    }

    public static boolean isValidGit(String git) {
        return !TextUtils.isEmpty(git) &&
                AppConfig.GIT_URL_VALIDATE.matcher(git).matches();
    }

    /**
     * Запрос фокуса у EditText и если возможно установка его и перемещение курсора
     * в конец строки
     *
     * @param editText EditText у которого запрашивается фокус
     */
    public void requestFocus(EditText editText) {
        if (editText.requestFocus()) {
            mActivityReference.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            editText.setSelection(editText.length());
        }
    }
}
