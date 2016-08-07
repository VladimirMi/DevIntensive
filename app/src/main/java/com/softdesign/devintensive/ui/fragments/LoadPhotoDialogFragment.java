package com.softdesign.devintensive.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

public class LoadPhotoDialogFragment extends DialogFragment {

    public interface OnDialogFragmentClickListener {
        void onFragmentClick(int action);
    }

    private OnDialogFragmentClickListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnDialogFragmentClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement listeners!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
                getString(R.string.user_profile_diallog_camera),
                getString(R.string.user_profile_dialog_cancel)};

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.user_profile_dialog_title);
        builder.setItems(selectItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choiceItem) {
                switch (choiceItem) {
                    case 0:
                        if (mListener != null) {
                            mListener.onFragmentClick(ConstantManager.LOAD_FROM_GALLERY);
                        }
                        break;
                    case 1:
                        if (mListener != null) {
                            mListener.onFragmentClick(ConstantManager.LOAD_FROM_CAMERA);
                        }
                        break;
                    case 2:
                        dialogInterface.cancel();
                        break;
                }
            }
        });
        return builder.create();
    }
}