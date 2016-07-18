package com.softdesign.devintensive.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.utils.ConstantManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * This Fragment manages a single background task and retains
 * itself across configuration changes.
 */
public class LoadUsersTaskFragment extends Fragment {

    private static final String TAG = ConstantManager.TAG_PREFIX + "LoadUsersTaskFragment";

    // Declare some sort of interface that your Call will use to communicate with the Activity
    public interface TaskCallbacks {
        void onResponse(Call<UserListRes> call, Response<UserListRes> response);

        void onFailure(Call<UserListRes> call, Throwable t);
    }

    private TaskCallbacks mListener;
    private Call<UserListRes> mCall;
    private Response<UserListRes> mResponse;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Try to use the Activity as a listener
        if (context instanceof TaskCallbacks) {
            mListener = (TaskCallbacks) context;
        } else {
            // You can decide if you want to mandate that the Activity implements your callback interface
            // in which case you should throw an exception if it doesn't:
            throw new IllegalStateException("Parent activity must implement TaskCallbacks");
            // or you could just swallow it and allow a state where nobody is listening
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this Fragment so that it will not be destroyed when an orientation
        // change happens and we can keep our AsyncTask running
        setRetainInstance(true);
    }

    /**
     * The Activity can call this when it wants to start the task
     */
    public void startLoad() {
        mCall = DataManager.getInstance().getUserList();

        mCall.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                mResponse = response;
                if (mListener != null) {
                    mListener.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                if (mListener != null) {
                    mListener.onFailure(call, t);
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // We still have to cancel the task in onDestroy because if the user exits the app or
        // finishes the Activity, we don't want the task to keep running
        // Since we are retaining the Fragment, onDestroy won't be called for an orientation change
        // so this won't affect our ability to keep the task running when the user rotates the device
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // This is VERY important to avoid a memory leak (because mListener is really a reference to an Activity)
        // When the orientation change occurs, onDetach will be called and since the Activity is being destroyed
        // we don't want to keep any references to it
        // When the Activity is being re-created, onAttach will be called and we will get our listener back
        mListener = null;
    }

    public Response<UserListRes> getResponse() {
        return mResponse;
    }
}