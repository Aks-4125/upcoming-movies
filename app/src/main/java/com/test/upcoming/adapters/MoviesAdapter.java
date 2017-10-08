package com.test.upcoming.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.upcoming.BuildConfig;
import com.test.upcoming.R;
import com.test.upcoming.Utils;
import com.test.upcoming.model.Movies;
import com.test.upcoming.networkcall.NetworkCheck;
import com.test.upcoming.ui.activity.detail.DetailActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aks4125 on 7/25/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {


    private Context context;
    private ArrayList<Movies.Result> mDataList;
    private Utils mUtils;
    private NetworkCheck networkCheck;

    public MoviesAdapter(Context context, ArrayList<Movies.Result> mDataList, Utils mUtils, NetworkCheck networkCheck) {
        this.context = context;
        this.mDataList = mDataList;
        this.mUtils = mUtils;
        this.networkCheck = networkCheck;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_card_movie, null);
        return new MovieHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieHolder holder, int position) {

        final Movies.Result mMovie = mDataList.get(position);
        Glide.with(context)
                .load(BuildConfig.BASE_IMG_URL + "w185" + mMovie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mMovieLogo);


        holder.mMovieName.setText(mMovie.getTitle()); // movie name
        holder.mReleaseDate.setText(mUtils.convertDateStringToString(mMovie.getReleaseDate(), "yyyy-MM-dd", "dd, MMM yyyy")); // movie date
        holder.mAdult.setText(mMovie.getAdult() ? "18+: Yes" : "18+: No"); // display 18+ if movie is adult


        holder.cvMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (networkCheck.isConnected())
                    context.startActivity(new Intent(context, DetailActivity.class).putExtra("movie", Parcels.wrap(mMovie)));
                else
                    mUtils.showSnackbarShort(context.getString(R.string.check_internet), holder.cvMovie);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mMovieLogo)
        ImageView mMovieLogo;
        @BindView(R.id.mMovieName)
        TextView mMovieName;
        @BindView(R.id.mReleaseDate)
        TextView mReleaseDate;
        @BindView(R.id.mAdult)
        TextView mAdult;
        @BindView(R.id.cvMovie)
        CardView cvMovie;

        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
