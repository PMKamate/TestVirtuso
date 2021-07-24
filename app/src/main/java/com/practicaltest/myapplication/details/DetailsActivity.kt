package com.practicaltest.myapplication.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.practicaltest.myapplication.R
import com.practicaltest.myapplication.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {
    private var mUrl: String? = null
    private var mTitle: String? = null
    private var mDate: String? = null
    private var mSource: String? = null
    private var isHideToolbarView = false
    private var id: String? = null
    private var tag: String? = null
    private var mImg: String? = null
    private var mAuthor: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsing_toolbar.setTitle("")
        appbar.addOnOffsetChangedListener(this)
        val intent: Intent = getIntent()
        id = intent.getStringExtra("id")
        setNewsData()

    }

    fun setNewsData() {
        mUrl = intent.getStringExtra("url")
        mImg = intent.getStringExtra("img")
        mTitle = intent.getStringExtra("title")
        mDate = intent.getStringExtra("date")
        mSource = intent.getStringExtra("source")
        mAuthor = intent.getStringExtra("author")
        val requestOptions = RequestOptions()
        requestOptions.error(Utils.getRandomDrawbleColor())
        Glide.with(this)
            .load(mImg)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(backdrop)
        title_on_appbar!!.text = mSource
        subtitle_on_appbar!!.text = mUrl
        date.setText(Utils.DateFormat(mDate))
        tvtitle.setText(mTitle)
        if (intent.hasExtra("tag")) {
            time.text = mAuthor
        } else {
            val author: String
            author = if (mAuthor != null) {
                " \u2022 $mAuthor"
            } else {
                ""
            }
            time!!.text = mSource + author + " \u2022 " + Utils.DateToTimeFormat(mDate)
        }

        initWebView(mUrl)
    }

    private fun initWebView(url: String?) {
        val webView: WebView = findViewById(R.id.webView)
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.webViewClient = WebViewClient()
        url?.let { webView.loadUrl(it) }
        progress_bar.visibility = View.GONE

    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val maxScroll: Int = appBarLayout.getTotalScrollRange()
        val percentage = Math.abs(verticalOffset).toFloat() / maxScroll.toFloat()
        if (percentage == 1f && isHideToolbarView) {
            date_behavior!!.visibility = View.GONE
            title_appbar!!.visibility = View.VISIBLE
            isHideToolbarView = !isHideToolbarView
        } else if (percentage < 1f && !isHideToolbarView) {
            date_behavior!!.visibility = View.VISIBLE
            title_appbar!!.visibility = View.GONE
            isHideToolbarView = !isHideToolbarView
        }
    }


}
