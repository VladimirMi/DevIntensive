package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.redmadrobot.chronos.ChronosOperation;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.data.tasks.LoadRepositories;
import com.softdesign.devintensive.data.tasks.LoadUsersList;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.ui.behaviors.CustomItemTouchHelperCallback;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.UiHelper;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";
    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;
    @BindView(R.id.user_list) RecyclerView mRecyclerView;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;

    private DataManager mDataManager;
    private PreferencesManager mPreferencesManager;
    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;
    private HashMap<String, List<Repository>> mRepositories;

    private boolean isAnimate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();
        mPreferencesManager = mDataManager.getPreferencesManager();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();

        ChronosOperation<List<User>> loadUsersListTask = new LoadUsersList();
        runOperation(loadUsersListTask);
        ChronosOperation<HashMap<String, List<Repository>>> loadRepositoriesTask = new LoadRepositories();
        runOperation(loadRepositoriesTask);

        if (savedInstanceState != null) {
            isAnimate = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mUsersAdapter.filter(newText);
                mRecyclerView.scrollToPosition(0);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mNavigationView.getMenu().getItem(2).setChecked(true);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mPreferencesManager.loadUserName());
        }
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        assert navigationView != null;
        navigationView.getMenu().getItem(2).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.login_menu:
                        mPreferencesManager.clearAllData();
                        mPreferencesManager.saveAuthToken(ConstantManager.INVALID_TOKEN);
                        Intent loginIntent = new Intent(UserListActivity.this, AuthActivity.class);
                        startActivity(loginIntent);
                        finish();
                        return true;
                    case R.id.user_profile_menu:
                        Intent userIntent = new Intent(UserListActivity.this, MainActivity.class);
                        userIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(userIntent);
                        return true;
                }
                return false;
            }
        });

        // установка круглого аватара
        ImageView userAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        UiHelper.setUserAvatar(this, mPreferencesManager.loadUserAvatar(), userAvatar);

        TextView userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name_txt);
        TextView userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email_txt);
        userName.setText(mPreferencesManager.loadUserName());
        userEmail.setText(mPreferencesManager.loadUserInfo().get(1));
    }

    private void setupRecycler() {
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUsers.get(position), mRepositories.get(mUsers.get(position).getRemoteId()));

                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                startActivity(profileIntent);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mUsersAdapter);

        if (isAnimate) {
            mRecyclerView.setTranslationY(UiHelper.getScreenHeight(this));
            mRecyclerView.setAlpha(0f);
            mRecyclerView.setScaleX(0.0f);
            mRecyclerView.setScaleY(0.0f);
            mRecyclerView.animate()
                    .translationY(0)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(1000)
                    .alpha(1f)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .start();
        }

        ItemTouchHelper.Callback callback = new CustomItemTouchHelperCallback(mUsersAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    public void onOperationFinished(final LoadUsersList.Result result) {
        if (result.isSuccessful()) {
            mUsers = result.getOutput();
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