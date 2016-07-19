package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.ui.views.RepositoryDeviderView;
import com.softdesign.devintensive.ui.views.RepositoryView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.UiHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.profile_photo) ImageView mProfileImage;
    @BindView(R.id.about_me_et) EditText mUserBio;
    @BindView(R.id.rating_txt) TextView mRating;
    @BindView(R.id.code_lines_txt) TextView mCodeLines;
    @BindView(R.id.projects_txt) TextView mProjects;
    @BindView(R.id.repositories_list) ListView mRepoListView;

    private UserDTO mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        ButterKnife.bind(this);

        mUser = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        setupToolbar();
        initRepositoriesView();
        initProfileData();
    }


    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRepositoriesView() {
//        final List<String> repositories = mUser.getRepositories();
//
//        RepositoriesAdapter adapter = new RepositoriesAdapter(repositories, new RepositoriesAdapter.CustomClickListener() {
//            @Override
//            public void onItemClickListener(Uri uri) {
//                Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(viewIntent);
//            }
//        });
//        mRepoListView.setAdapter(adapter);
//        UiHelper.setRepoListViewHeight(mRepoListView);
    }


    private void initProfileData() {
        mRating.setText(mUser.getRating());
        mCodeLines.setText(mUser.getCodeLines());
        mProjects.setText(mUser.getProjects());
        mUserBio.setText(mUser.getBio());
        mCollapsingToolbar.setTitle(mUser.getFullName());

        Picasso.with(this)
                .load(mUser.getPhoto())
                .placeholder(R.drawable.user_bg)
                .error(R.drawable.user_bg)
                .into(mProfileImage);
    }
}
