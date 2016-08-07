package com.softdesign.devintensive.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.redmadrobot.chronos.ChronosOperation;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.Like;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.data.tasks.LoadRepositories;
import com.softdesign.devintensive.data.tasks.LoadUsersList;
import com.softdesign.devintensive.ui.activities.MainActivity;
import com.softdesign.devintensive.ui.activities.ProfileUserActivity;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.ui.behaviors.CustomItemTouchHelperCallback;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends BaseFragment {

    private static final String TAG = ConstantManager.TAG_PREFIX + "UserListFragment";

    @BindView(R.id.user_list) RecyclerView mRecyclerView;

    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;
    private List<User> mUsersCopy;
    private HashMap<String, List<Repository>> mRepositories;

    private MainActivity mActivity;

    private TimerTask mSearchTask;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setNestedScrollingEnabled(false);
        setupToolbar();

        ChronosOperation<List<User>> loadUsersListTask = new LoadUsersList();
        runOperation(loadUsersListTask);
        ChronosOperation<HashMap<String, List<Repository>>> loadRepositoriesTask = new LoadRepositories();
        runOperation(loadRepositoriesTask);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                search(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupToolbar() {
        ActionBar actionBar = mActivity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mPreferencesManager.loadUserName());
        }
    }

    private void setupRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onMoreInfoClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUsers.get(position), mRepositories.get(mUsers.get(position).getRemoteId()));

                Intent profileIntent = new Intent(mActivity, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                startActivity(profileIntent);
            }

            @Override
            public void onLikeClickListener(int position, View view) {
                setLike(position, view);
            }
        });
        mRecyclerView.setAdapter(mUsersAdapter);

        ItemTouchHelper.Callback callback = new CustomItemTouchHelperCallback(mUsersAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void setLike(final int position, final View view) {
        if (NetworkStatusChecker.isNetworkAvaliable(mContext)) {

            List<Like> likes = mDataManager.getDaoSession().getLikeDao()
                    ._queryUser_LikesBy(mUsers.get(position).getRemoteId());
            Like userLike = null;

            for (Like like : likes) {
                if (like.getSubjectRemoteId().equals(mPreferencesManager.getUserId())) {
                    userLike = like;
                }
            }

            if (userLike == null) {

                Call<ResponseBody> call = mDataManager.setLike(mUsers.get(position).getRemoteId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        mDataManager.getDaoSession().getLikeDao().insertOrReplace(
                                new Like(mUsers.get(position).getRemoteId(), mPreferencesManager.getUserId()));

                        mUsersAdapter.notifyItemChanged(position);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            } else {

                Call<ResponseBody> call = mDataManager.deleteLike(mUsers.get(position).getRemoteId());
                final Like finalUserLike = userLike;
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        mDataManager.getDaoSession().getLikeDao().delete(finalUserLike);
                        mUsersAdapter.notifyItemChanged(position);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }
    }


    public void search(final String query) {
        Timer searchTimer = new Timer();

        if (mSearchTask != null) {
            mSearchTask.cancel();
        }

        mSearchTask = new TimerTask() {
            @Override
            public void run() {
                mUsers = mDataManager.searchUsers(query.toUpperCase());
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swapAdapter();
                    }
                });
            }
        };
        if (query.isEmpty()) {
            mUsers = mUsersCopy;
            swapAdapter();
        } else {
            searchTimer.schedule(mSearchTask, AppConfig.SEARCH_DELAY);
        }
    }

    private void swapAdapter() {
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onMoreInfoClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUsers.get(position), mRepositories.get(mUsers.get(position).getRemoteId()));

                Intent profileIntent = new Intent(mActivity, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                startActivity(profileIntent);
            }

            @Override
            public void onLikeClickListener(int position, View view) {
                setLike(position, view);
            }
        });
        mRecyclerView.swapAdapter(mUsersAdapter, false);

        ItemTouchHelper.Callback callback = new CustomItemTouchHelperCallback(mUsersAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    public void onOperationFinished(final LoadUsersList.Result result) {
        if (result.isSuccessful()) {
            mUsers = result.getOutput();
            mUsersCopy = result.getOutput();
            setupRecycler();
        } else {
            Log.e(TAG, "onSaveUserListFinished: " + result.getErrorMessage());
        }
    }

    public void onOperationFinished(final LoadRepositories.Result result) {
        if (result.isSuccessful()) {
            mRepositories = result.getOutput();
        } else {

            Log.e(TAG, "onSaveUserListFinished: " + result.getErrorMessage());
        }
    }
}