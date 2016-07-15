package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private UserViewHolder.CustomClickListener mListener;
    Context mContext;
    List<UserListRes.UserData> mUsers;

    public UsersAdapter(List<UserListRes.UserData> users, UserViewHolder.CustomClickListener clickListener) {
        mUsers = users;
        mListener = clickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View converView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        mContext = parent.getContext();
        return new UserViewHolder(converView, mListener);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserListRes.UserData user = mUsers.get(position);

        if (!user.getPublicInfo().getPhoto().isEmpty()) {
            Picasso.with(mContext)
                    .load(user.getPublicInfo().getPhoto())
                    .resize(512, 256)
                    .centerCrop()
                    .placeholder(mContext.getResources().getDrawable(R.drawable.user_bg))
                    .error(mContext.getResources().getDrawable(R.drawable.user_bg))
                    .into(holder.mUserPhoto);
        }

        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getLinesCode()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getProjects()));

        if (user.getPublicInfo().getBio() == null || user.getPublicInfo().getBio().isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getPublicInfo().getBio());
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public UserListRes.UserData removeItem(int position) {
        final UserListRes.UserData model = mUsers.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, UserListRes.UserData model) {
        mUsers.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final UserListRes.UserData user = mUsers.remove(fromPosition);
        mUsers.add(toPosition, user);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<UserListRes.UserData> users) {
        applyAndAnimateRemovals(users);
        applyAndAnimateAdditions(users);
        applyAndAnimateMovedItems(users);
    }

    private void applyAndAnimateRemovals(List<UserListRes.UserData> newModels) {
        for (int i = mUsers.size() - 1; i >= 0; i--) {
            final UserListRes.UserData user = mUsers.get(i);
            if (!newModels.contains(user)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<UserListRes.UserData> newUsers) {
        for (int i = 0, count = newUsers.size(); i < count; i++) {
            final UserListRes.UserData user = newUsers.get(i);
            if (!mUsers.contains(user)) {
                addItem(i, user);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<UserListRes.UserData> newUsers) {
        for (int toPosition = newUsers.size() - 1; toPosition >= 0; toPosition--) {
            final UserListRes.UserData user = newUsers.get(toPosition);
            final int fromPosition = mUsers.indexOf(user);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.user_photo) AspectRatioImageView mUserPhoto;
        @BindView(R.id.user_full_name_txt) TextView mFullName;
        @BindView(R.id.rating_txt) TextView mRating;
        @BindView(R.id.code_lines_txt) TextView mCodeLines;
        @BindView(R.id.projects_txt) TextView mProjects;
        @BindView(R.id.bio_txt) TextView mBio;
        @BindView(R.id.more_info_btn) Button mShowMore;

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
