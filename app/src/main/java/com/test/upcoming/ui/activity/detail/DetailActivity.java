package com.test.upcoming.ui.activity.detail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.test.upcoming.BaseActivity;
import com.test.upcoming.BuildConfig;
import com.test.upcoming.R;
import com.test.upcoming.adapters.ImageAdapter;
import com.test.upcoming.customui.CirclePageIndicator;
import com.test.upcoming.model.MovieImages;
import com.test.upcoming.model.Movies;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity implements DetailContractor.IDetailView {

    @Inject
    DetailPresenter mPresenter;

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.tvMovietitle)
    TextView tvMovietitle;
    @BindView(R.id.tvMovieOverview)
    TextView tvMovieOverview;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.movieDetails)
    LinearLayout movieDetails;
    @BindView(R.id.myInfo)
    ImageView myInfo;
    private Movies.Result movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView();

        ButterKnife.bind(this);
        getComponent().inject(this);
        mPresenter = new DetailPresenter(webService);
        mPresenter.setDetailView(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("movie")) {

            movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
            mPresenter.getMovieImages(BuildConfig.API_KEY, String.valueOf(movie.getId()));
            tvMovietitle.setText(movie.getTitle());
            tvMovieOverview.setText(movie.getOverview());
            tvMovieOverview.setMovementMethod(new ScrollingMovementMethod());

            getSupportActionBar().setTitle(movie.getTitle()); // Action bar title is not supposed to be long text. if so, we can use Toolbar. :)
            Double rate = movie.getPopularity();
            int avgRating = ((int) rate.doubleValue());

            if (avgRating < 20)
                ratingBar.setRating(1);
            else if (avgRating < 40)
                ratingBar.setRating(2);
            else if (avgRating < 50)
                ratingBar.setRating(3);
            else if (avgRating < 80)
                ratingBar.setRating(4);
            else if (avgRating > 81)
                ratingBar.setRating(5);

        }

    }

    //actionbar back button action
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_detail;
    }


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.GONE);
        movieDetails.setVisibility(View.GONE);
    }

    @Override
    public void stopProgress() {
        progressBar.setVisibility(View.GONE);
        movieDetails.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(pager, message, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void processJson(JsonObject object) {
        MovieImages images = mGson.fromJson(object, MovieImages.class);
        pager.setAdapter(new ImageAdapter(this, images.getBackdrops()));

        indicator.setViewPager(pager);
    }

    @OnClick(R.id.myInfo)
    public void onViewClicked() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("About Me:");
        alert.setMessage("");
        WebView wv = new WebView(this);
        wv.loadUrl("https://about.me/akash.bhatt");
//        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//
//                return true;
//            }
//        });
        wv.setWebChromeClient(new WebChromeClient());
        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
        Toast.makeText(this, "Loading about me", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.unsubscribe();

        super.onDestroy();
    }
}
