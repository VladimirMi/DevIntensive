package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private static final String TAG = ConstantManager.TAG_PREFIX + "UsersAdapter";
    private UserViewHolder.CustomClickListener mListener;
    private Context mContext;
    private List<User> mUsers;
    private List<User> mUsersCopy = new ArrayList<>();

    public UsersAdapter(List<User> users, UserViewHolder.CustomClickListener clickListener) {
        mUsers = users;
        mUsersCopy.addAll(users);
        mListener = clickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View converView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        mContext = parent.getContext();
        return new UserViewHolder(converView, mListener);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final User user = mUsers.get(position);
        final String userPhoto;
        if (user.getPhoto().isEmpty()) {
            userPhoto = "null";
            Log.e(TAG, "onBindViewHolder: user with name " + user.getFullName() + " has empty photo");
        } else {
            userPhoto = user.getPhoto();
        }

        Picasso.with(mContext)
                .load(userPhoto)
                .fit()
                .centerCrop()
                .placeholder(holder.mDummy)
                .error(holder.mDummy)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.mUserPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Load from cache");
                    }

                    @Override
                    public void onError() {
                        DataManager.getInstance().getPicasso()
                                .load(userPhoto)
                                .fit()
                                .centerCrop()
                                .placeholder(holder.mDummy)
                                .error(holder.mDummy)
                                .into(holder.mUserPhoto, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(TAG, "Load from net");
                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, "Could not fetch the image");
                                    }
                                });
                    }
                });


        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getCodeLines()));
        holder.mProjects.setText(String.valueOf(user.getProjects()));

        if (user.getBio() == null || user.getBio().isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getBio());
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void filter(String query) {
        if (query.isEmpty()) {
            mUsers.clear();
            mUsers.addAll(mUsersCopy);
        } else {
            ArrayList<User> newUsers = new ArrayList<>();
            query = query.toLowerCase();
            for (User user : mUsersCopy) {
                if (user.getFullName().toLowerCase().contains(query)) {
                    newUsers.add(user);
                }
            }
            mUsers.clear();
            mUsers.addAll(newUsers);
        }
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.user_photo) AspectRatioImageView mUserPhoto;
        @BindView(R.id.user_full_name_txt) TextView mFullName;
        @BindView(R.id.rating_txt) TextView mRating;
        @BindView(R.id.code_lines_txt) TextView mCodeLines;
        @BindView(R.id.projects_txt) TextView mProjects;
        @BindView(R.id.bio_txt) TextView mBio;
        @BindView(R.id.more_info_btn) Button mShowMore;
        @BindDrawable(R.drawable.user_bg) Drawable mDummy;

        private CustomClickListener mListener;

        public UserViewHolder(View itemView, CustomClickListener clickListener) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mListener = clickListener;
            mShowMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onUserItemClickListener(getAdapterPosition());
            }
        }

        public interface CustomClickListener {
            void onUserItemClickListener(int position);
        }
    }
}
