package com.test.upcoming.adapters

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.test.upcoming.BuildConfig
import com.test.upcoming.R
import com.test.upcoming.model.MovieImages

import java.util.ArrayList

/**
 * Created by Aks4125 on 7/26/2017.
 */

class ImageAdapter(private val context: Context, private val IMAGES: ArrayList<MovieImages.Backdrop>) : PagerAdapter() {
    private val inflater: LayoutInflater


    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.layout_images, view, false)


        val imageView = imageLayout!!
                .findViewById(R.id.imgMovies) as ImageView

        Glide.with(context)
                .load(BuildConfig.BASE_IMG_URL + "w342" + IMAGES[position].filePath)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.ic_crop_original_black_24dp)
                .centerCrop()
                .into(imageView)

        imageView.setOnClickListener(null)
        imageLayout.isClickable = false
        imageView.isClickable = false
        view.isClickable = false
        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}
