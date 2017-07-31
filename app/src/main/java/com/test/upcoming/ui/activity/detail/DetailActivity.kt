package com.test.upcoming.ui.activity.detail

import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast


import com.google.gson.JsonObject
import com.test.upcoming.BaseActivity
import com.test.upcoming.BuildConfig
import com.test.upcoming.R
import com.test.upcoming.adapters.ImageAdapter
import com.test.upcoming.customui.CirclePageIndicator
import com.test.upcoming.model.MovieImages
import com.test.upcoming.model.Movies

import org.parceler.Parcels

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class DetailActivity : BaseActivity(), DetailContractor.IDetailView {

    @Inject
    internal var mPresenter: DetailPresenter? = null

    @BindView(R.id.pager)
    internal var pager: ViewPager? = null
    @BindView(R.id.indicator)
    internal var indicator: CirclePageIndicator? = null
    @BindView(R.id.linearLayout)
    internal var linearLayout: LinearLayout? = null
    @BindView(R.id.tvMovietitle)
    internal var tvMovietitle: TextView? = null
    @BindView(R.id.tvMovieOverview)
    internal var tvMovieOverview: TextView? = null
    @BindView(R.id.ratingBar)
    internal var ratingBar: RatingBar? = null
    @BindView(R.id.progressBar)
    internal var progressBar: ProgressBar? = null
    @BindView(R.id.movieDetails)
    internal var movieDetails: LinearLayout? = null
    @BindView(R.id.myInfo)
    internal var myInfo: ImageView? = null
    private var movie: Movies.Result? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  setContentView();

        ButterKnife.bind(this)
        component!!.inject(this)
        mPresenter = DetailPresenter(webService)
        mPresenter!!.setDetailView(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("movie")) {

            movie = Parcels.unwrap<Movies.Result>(intent.getParcelableExtra<Parcelable>("movie"))
            mPresenter!!.getMovieImages(BuildConfig.API_KEY, movie!!.id.toString())
            tvMovietitle!!.text = movie!!.title
            tvMovieOverview!!.text = movie!!.overview
            tvMovieOverview!!.movementMethod = ScrollingMovementMethod()

            supportActionBar!!.setTitle(movie!!.title) // Action bar title is not supposed to be long text. if so, we can use Toolbar. :)
            val rate = movie!!.popularity
            val avgRating = rate!!.toDouble().toInt()

            if (avgRating < 20)
                ratingBar!!.rating = 1f
            else if (avgRating < 40)
                ratingBar!!.rating = 2f
            else if (avgRating < 50)
                ratingBar!!.rating = 3f
            else if (avgRating < 80)
                ratingBar!!.rating = 4f
            else if (avgRating > 81)
                ratingBar!!.rating = 5f

        }

    }

    //actionbar back button action
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override val layoutResourceId: Int
        get() = R.layout.activity_detail


    override fun showProgress() {
        progressBar!!.visibility = View.VISIBLE
        ratingBar!!.visibility = View.GONE
        movieDetails!!.visibility = View.GONE
    }

    override fun stopProgress() {
        progressBar!!.visibility = View.GONE
        movieDetails!!.visibility = View.VISIBLE
        ratingBar!!.visibility = View.VISIBLE
    }

    override fun showMessage(message: String) {
        Snackbar.make(ratingBar!!, message, Snackbar.LENGTH_SHORT).show()

    }

    override fun processJson(`object`: JsonObject) {
        val images = mGson!!.fromJson<MovieImages>(`object`, MovieImages::class.java!!)
        pager!!.adapter = ImageAdapter(this, images.backdrops)

        indicator!!.setViewPager(pager)
    }

    @OnClick(R.id.myInfo)
    fun onViewClicked() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("About Me:")
        alert.setMessage("")
        val wv = WebView(this)
        wv.loadUrl("https://about.me/akash.bhatt")
        //        wv.setWebViewClient(new WebViewClient() {
        //            @Override
        //            public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //                view.loadUrl(url);
        //
        //                return true;
        //
        //        });
        wv.setWebChromeClient(WebChromeClient())
        alert.setView(wv)
        alert.setNegativeButton("Close") { dialog, id -> dialog.dismiss() }
        alert.show()
        Toast.makeText(this, "Loading about me", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        if (mPresenter != null)
            mPresenter!!.unsubscribe()

        super.onDestroy()
    }
}
