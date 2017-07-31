package com.test.upcoming.ui.activity.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar

import com.test.upcoming.BaseActivity
import com.test.upcoming.BuildConfig
import com.test.upcoming.R
import com.test.upcoming.adapters.MoviesAdapter
import com.test.upcoming.model.Movies
import com.google.gson.JsonObject

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife

class MainActivity : BaseActivity(), MainContractor.IMainView {


    @Inject
    internal var mainPresenter: MainPresenter? = null

    @BindView(R.id.rvMovieList)
    internal var rvMovieList: RecyclerView? = null
    @BindView(R.id.progressBar)
    internal var progressBar: ProgressBar? = null
    @BindView(R.id.swiperefresh)
    internal var swiperefresh: SwipeRefreshLayout? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main);
        ButterKnife.bind(this)
        component!!.inject(this)
        mLayoutManager = LinearLayoutManager(this)
        mainPresenter!!.setMainView(this)
        swiperefresh!!.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
        if (networkCheck!!.isConnected)
            loadMovies()
        else
            mUtils!!.showSnackbarShort(getString(R.string.check_internet), swiperefresh)


        swiperefresh!!.setOnRefreshListener { loadMovies() }
    }

    override fun onDestroy() {
        if (mainPresenter != null)
            mainPresenter!!.unsubscribe()

        super.onDestroy()
    }

    private fun loadMovies() {
        mainPresenter!!.getMovieList(BuildConfig.API_KEY)
    }

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override fun showProgress() {
        //progressBar.setVisibility(View.VISIBLE);
        rvMovieList!!.visibility = View.GONE
        swiperefresh!!.isRefreshing = true
    }

    override fun stopProgress() {
        swiperefresh!!.isRefreshing = false
        // progressBar.setVisibility(View.GONE);
        rvMovieList!!.visibility = View.VISIBLE

    }

    override fun showMessage(message: String) {
        Snackbar.make(rvMovieList!!, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun processJson(`object`: JsonObject) {
        val movies = mGson!!.fromJson(`object`, Movies::class.java)
        rvMovieList!!.layoutManager = mLayoutManager
        rvMovieList!!.adapter = MoviesAdapter(this, movies.results, mUtils!!, networkCheck!!)

    }


}
