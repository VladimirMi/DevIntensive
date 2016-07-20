package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.ui.activities.MainActivity;

public class MyTextWatcher implements TextWatcher {
    private final EditText mEditText;
    private final TextInputLayout mInputLayout;
    private final ImageView mActionIcon;
    private MainActivity mActivityReference;

    public MyTextWatcher(MainActivity activity, EditText editText, ImageView actionIcon) {
        mEditText = editText;
        mInputLayout = (TextInputLayout) mEditText.getParent();
        mActionIcon = actionIcon;
        mActivityReference = activity;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        switch (mEditText.getId()) {
            case R.id.phone_et:
                validatePhone(mEditText);
                break;
            case R.id.email_et:
                validateEmail(mEditText);
                break;
            case R.id.vk_et:
                validateVk(mEditText);
                break;
            case R.id.github_et:
                validateGit(mEditText);
                break;
        }
    }

    private boolean validatePhone(EditText phone) {
        return setupValidate(isValid(phone),
                DataManager.getInstance().getContext().getString(R.string.phone_hint),
                mActivityReference.getString(R.string.err_msg_phone),
                R.drawable.ic_phone_in_talk_24dp,
                R.drawable.ic_error_24dp);
    }

    private boolean validateEmail(EditText email) {
        return setupValidate(isValid(email),
                mActivityReference.getString(R.string.email_hint),
                mActivityReference.getString(R.string.err_msg_email),
                R.drawable.ic_send_24dp,
                R.drawable.ic_error_24dp);
    }

    private boolean validateVk(EditText vk) {
        return setupValidate(isValid(vk),
                mActivityReference.getString(R.string.vk_hint),
                mActivityReference.getString(R.string.err_msg_vk),
                R.drawable.ic_visibility_24dp,
                R.drawable.ic_error_24dp);
    }

    private boolean validateGit(EditText git) {
        return setupValidate(isValid(git),
                mActivityReference.getString(R.string.github_hint),
                mActivityReference.getString(R.string.err_msg_git),
                R.drawable.ic_visibility_24dp,
                R.drawable.ic_error_24dp);
    }

    private boolean setupValidate(Boolean isValid, String hint, String errMsg, int actionIcon, int errIcon) {
        if (isValid) {
            mInputLayout.setHint(hint);
            mActionIcon.setImageResource(actionIcon);
            return true;
        } else {
            mInputLayout.setHint(errMsg);
            mActionIcon.setImageResource(errIcon);
            return false;
        }
    }

    public static boolean isValid(EditText editText) {
        String tag = String.valueOf(editText.getTag());
        String value = editText.getText().toString().trim();
        Context context = DataManager.getInstance().getContext();

        if (value.isEmpty()) {
            return false;
        }

        if (tag.equals(context.getString(R.string.phone_tag))) {
            return AppConfig.PHONE_VALIDATE.matcher(value).matches();
        }
        if (tag.equals(context.getString(R.string.email_tag))) {
            return AppConfig.EMAIL_ADDRESS_VALIDATE.matcher(value).matches();
        }
        if (tag.equals(context.getString(R.string.vk_tag))) {
            return AppConfig.VK_URL_VALIDATE.matcher(value).matches();
        }
        if (tag.equals(context.getString(R.string.git_tag))) {
            return AppConfig.GIT_URL_VALIDATE.matcher(value).matches();
        }

        return true;
    }

    /**
     * Запрос фокуса у EditText и если возможно, установка его и перемещение курсора
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
