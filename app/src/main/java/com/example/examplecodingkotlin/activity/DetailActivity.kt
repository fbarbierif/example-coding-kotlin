package com.example.examplecodingkotlin.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.examplecodingkotlin.R
import java.util.Objects

/**
 * Created by fbarbieri on 2019-08-30.
 */
class DetailActivity : AppCompatActivity() {

    internal lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val title = intent.getStringExtra(TITLE)

        title?.let {
            setupActionBar(it)
        }

        val url = intent.getStringExtra(URL)

        progressBar = findViewById(R.id.progressBar)
        val wbRecipeDetail = findViewById<WebView>(R.id.wbRecipeDetail)

        progressBar.visibility = View.VISIBLE

        wbRecipeDetail.webViewClient = CustomWebViewClient()
        wbRecipeDetail.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress == PROGRESS_COMPLETED) {
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
        wbRecipeDetail.loadUrl(url)
    }

    private class CustomWebViewClient internal constructor() : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

    private fun setupActionBar(title: String) {
        Objects.requireNonNull<ActionBar>(supportActionBar)
                .setTitle(title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        private val URL = "url"
        private val TITLE = "title"
        private val PROGRESS_COMPLETED = 100
    }
}
