package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.ui.behaviors.CustomItemTouchHelperCallback;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.UiHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>
        implements CustomItemTouchHelperCallback.ItemTouchHelperAdapter{

    private static final String TAG = ConstantManager.TAG_PREFIX + "UsersAdapter";
    private UserViewHolder.CustomClickListener mListener;
    private Context mContext;
    private List<User> mUsers = new ArrayList<>();
    private List<User> mUsersCopy = new ArrayList<>();


    public UsersAdapter(List<User> users, UserViewHolder.CustomClickListener clickListener) {
        mUsers = users;
        mUsersCopy = users;
        mListener = clickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View converView = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false);
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

        UiHelper.setUserPhoto(mContext, userPhoto, holder.mUserPhoto);

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

    public void filter(final String query) {
        if (query.isEmpty()) {
            mUsers = mUsersCopy;
            notifyDataSetChanged();
        } else {
            mUsers = DataManager.getInstance().searchUsers(query.toUpperCase());
            Handler handler= new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, AppConfig.SEARCH_DELAY);
        }
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        User user1 = mUsers.get(fromPosition);
        User user2 = mUsers.get(toPosition);
        user1.setOrder(toPosition);
        user2.setOrder(fromPosition);
        user1.update();
        user2.update();

        Collections.swap(mUsers, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        User user = mUsers.get(position);
        user.setDeleted(true);
        user.update();
        mUsers.remove(position);
        notifyItemRemoved(position);
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
