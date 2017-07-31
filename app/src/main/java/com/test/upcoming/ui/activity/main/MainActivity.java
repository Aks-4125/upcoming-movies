package com.test.upcoming.ui.activity.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.test.upcoming.BaseActivity;
import com.test.upcoming.BuildConfig;
import com.test.upcoming.R;
import com.test.upcoming.adapters.MoviesAdapter;
import com.test.upcoming.model.Movies;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainContractor.IMainView {


    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.rvMovieList)
    RecyclerView rvMovieList;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getComponent().inject(this);
        mLayoutManager = new LinearLayoutManager(this);
        mainPresenter.setMainView(this);
        swiperefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        if (networkCheck.isConnected())
            loadMovies();
        else
            mUtils.showSnackbarShort(getString(R.string.check_internet), swiperefresh);


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMovies();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mainPresenter != null)
            mainPresenter.unsubscribe();

        super.onDestroy();
    }

    private void loadMovies() {
        mainPresenter.getMovieList(BuildConfig.API_KEY);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void showProgress() {
        //progressBar.setVisibility(View.VISIBLE);
        rvMovieList.setVisibility(View.GONE);
        swiperefresh.setRefreshing(true);
    }

    @Override
    public void stopProgress() {
        swiperefresh.setRefreshing(false);
        // progressBar.setVisibility(View.GONE);
        rvMovieList.setVisibility(View.VISIBLE);

    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(rvMovieList, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void processJson(Movies movies) {
        //Movies movies = mGson.fromJson(object, Movies.class);
        rvMovieList.setLayoutManager(mLayoutManager);
        rvMovieList.setAdapter(new MoviesAdapter(this, movies.getResults(), mUtils, networkCheck));

    }


}
