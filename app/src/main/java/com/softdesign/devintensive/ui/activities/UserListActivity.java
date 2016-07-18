package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.ui.fragments.LoadUsersTaskFragment;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, LoadUsersTaskFragment.TaskCallbacks {

    private static final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";
    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;
    @BindView(R.id.user_list) RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private PreferencesManager mPreferencesManager;
    private UsersAdapter mUsersAdapter;
    private List<UserListRes.UserData> mUsers;
    private UserListRes mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();
        mPreferencesManager = mDataManager.getPreferencesManager();

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        setupToolbar();
        setupDrawer();

        FragmentManager fm = getSupportFragmentManager();
        LoadUsersTaskFragment loadUsersTask = (LoadUsersTaskFragment) fm.findFragmentByTag(ConstantManager.LOAD_FRAG_TAG);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (loadUsersTask == null) {
            loadUsersTask = new LoadUsersTaskFragment();
            fm.beginTransaction().add(loadUsersTask, ConstantManager.LOAD_FRAG_TAG).commit();
            loadUsersTask.startLoad();
        } else {
            mUsers = loadUsersTask.getResponse().body().getData();
            setupRecycler();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ConstantManager.USERS_LIST_KEY, new Gson().toJson(mResponse));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    private void loadUsers() {
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitle(mPreferencesManager.loadUserName());
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.login_menu:
                        mPreferencesManager.saveUserId("");
                        mPreferencesManager.saveAuthToken("");
                        Intent loginIntent = new Intent(UserListActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                        return true;
                    case R.id.user_profile_menu:
                        Intent usersIntent = new Intent(UserListActivity.this, MainActivity.class);
                        startActivity(usersIntent);
                        return true;
                }
                return false;
            }
        });
    }

    private void setupRecycler() {
        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUsers.get(position));
                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                startActivity(profileIntent);
            }
        });
        mRecyclerView.setAdapter(mUsersAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        mUsersAdapter.filter(query);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
        try {
            mUsers = response.body().getData();
            setupRecycler();
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onFailure(Call<UserListRes> call, Throwable t) {
        // TODO: 7/15/2016 обработать ошибки
    }
}
