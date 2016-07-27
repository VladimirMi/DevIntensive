package com.softdesign.devintensive.data.network;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class PicassoCache {
    private Context mCotext;
    private Picasso mPicassoInstance;

    public PicassoCache(Context cotext) {
        mCotext = cotext;

        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(cotext, Integer.MAX_VALUE);
        Picasso.Builder builder = new Picasso.Builder(cotext);
        builder.downloader(okHttp3Downloader);

        mPicassoInstance = builder.build();
        Picasso.setSingletonInstance(mPicassoInstance);
    }

    public Picasso getPicassoInstance() {

        if (mPicassoInstance == null) {
            new PicassoCache(mCotext);
        }

        return mPicassoInstance;
    }
}
