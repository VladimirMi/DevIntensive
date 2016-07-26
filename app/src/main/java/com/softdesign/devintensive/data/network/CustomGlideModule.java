package com.softdesign.devintensive.data.network;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;

public class CustomGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        int customBitmapPoolSize = 2 * defaultBitmapPoolSize;
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        int cacheSize100MegaBytes = 104857600;

        builder.setDiskCache(
                new DiskLruCacheFactory(DataManager.getInstance().getContext().getCacheDir().getPath(), cacheSize100MegaBytes));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }

    public static void setUserPhoto(final Context context, final String path, final ImageView view) {
        String loadPath = path.isEmpty() ? "null" : path;

        Glide.with(context)
                .load(loadPath)
                .error(context.getResources().getDrawable(R.drawable.user_bg))
                .placeholder(context.getResources().getDrawable(R.drawable.user_bg))
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(view);
    }

    public static void setUserAvatar(final Context context, final String path, final ImageView view) {
        String loadPath = path.isEmpty() ? "null" : path;

        Glide.with(context)
                .load(loadPath)
                .asBitmap()
                .centerCrop()
                .error(context.getResources().getDrawable(R.drawable.user_bg))
                .placeholder(context.getResources().getDrawable(R.drawable.user_bg))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new BitmapImageViewTarget(view) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        view.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}