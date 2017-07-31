package com.test.upcoming.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.test.upcoming.BuildConfig
import com.test.upcoming.R
import com.test.upcoming.Utils
import com.test.upcoming.model.Movies
import com.test.upcoming.networkcall.NetworkCheck
import com.test.upcoming.ui.activity.detail.DetailActivity

import org.parceler.Parcels

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife

/**
 * Created by Aks4125 on 7/25/2017.
 */

class MoviesAdapter(private val context: Context, private val mDataList: ArrayList<Movies.Result>, private val mUtils: Utils, private val networkCheck: NetworkCheck) : RecyclerView.Adapter<MoviesAdapter.MovieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_card_movie, null)
        return MovieHolder(v)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {

        val mMovie = mDataList[position]
        Glide.with(context)
                .load(BuildConfig.BASE_IMG_URL + "w185" + mMovie.posterPath)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mMovieLogo!!)


        holder.mMovieName!!.text = mMovie.title // movie name
        holder.mReleaseDate!!.text = mUtils.convertDateStringToString(mMovie.releaseDate, "yyyy-dd-mm", "dd, MMM yyyy") // movie date
        holder.mAdult!!.text = if (mMovie.adult) "18+: Yes" else "18+: No" // display 18+ if movie is adult


        holder.cvMovie!!.setOnClickListener {
            if (networkCheck.isConnected)
                context.startActivity(Intent(context, DetailActivity::class.java).putExtra("movie", Parcels.wrap(mMovie)))
            else
                mUtils.showSnackbarShort(context.getString(R.string.check_internet), holder.cvMovie)
        }

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.mMovieLogo)
        internal var mMovieLogo: ImageView? = null
        @BindView(R.id.mMovieName)
        internal var mMovieName: TextView? = null
        @BindView(R.id.mReleaseDate)
        internal var mReleaseDate: TextView? = null
        @BindView(R.id.mAdult)
        internal var mAdult: TextView? = null
        @BindView(R.id.cvMovie)
        internal var cvMovie: CardView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
