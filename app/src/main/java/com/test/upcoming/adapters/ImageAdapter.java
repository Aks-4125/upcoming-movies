package com.test.upcoming.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.upcoming.BuildConfig;
import com.test.upcoming.R;
import com.test.upcoming.model.MovieImages;

import java.util.ArrayList;

/**
 * Created by Aks4125 on 7/26/2017.
 */

public class ImageAdapter extends PagerAdapter {

    private ArrayList<MovieImages.Backdrop> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public ImageAdapter(Context context, ArrayList<MovieImages.Backdrop> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.layout_images, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imgMovies);

        Glide.with(context)
                .load(BuildConfig.BASE_IMG_URL + "w342" + IMAGES.get(position).getFilePath())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.ic_crop_original_black_24dp)
                .centerCrop()
                .into(imageView);

        imageView.setOnClickListener(null);
        imageLayout.setClickable(false);
        imageView.setClickable(false);
        view.setClickable(false);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
