package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class RepositoriesAdapter extends BaseAdapter implements View.OnClickListener {
    private final CustomClickListener mListener;
    private List<String> mRepoList;
    private List<EditText> mGitEtList = new ArrayList<>();
    private List<ImageView> mGitImgList = new ArrayList<>();

    public RepositoriesAdapter(List<String> repositories, CustomClickListener listener) {
        mRepoList = repositories;
        mListener = listener;
    }

    public List<EditText> getGitEtList() {
        return mGitEtList;
    }

    public List<ImageView> getGitImgList() {
        return mGitImgList;
    }

    @Override
    public int getCount() {
        return mRepoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRepoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (itemView == null) {
            itemView = layoutInflater.inflate(R.layout.item_repositories_list, parent, false);
        }

        EditText repoName = (EditText) itemView.findViewById(R.id.github_et);
        repoName.setText(mRepoList.get(position));

        ImageView gitIcon = (ImageView) itemView.findViewById(R.id.github_img);
        gitIcon.setContentDescription(mRepoList.get(position));
        gitIcon.setOnClickListener(this);

        mGitEtList.add(repoName);
        mGitImgList.add(gitIcon);

        return itemView;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClickListener(Uri.parse(ConstantManager.HTTPS_SCHEME + ((ImageView) v).getContentDescription()));
        }
    }

    public interface CustomClickListener {
        void onItemClickListener(Uri uri);
    }
}