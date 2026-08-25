package com.cardleyreminder.apps.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cardleyreminder.apps.R

class WebViewActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webView: WebView = findViewById(R.id.webView)
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val btnClose: ImageView = findViewById(R.id.btnClose)

        // Get URL from Intent
        val url = intent.getStringExtra("URL")
        val title = intent.getStringExtra("title")

        tvTitle.text=title?:""


        // Configure WebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient() // Open links in WebView, not external browser

        // Load the URL
        url?.let { webView.loadUrl(it) }

        // Close button to exit activity
        btnClose.setOnClickListener {
            finish()
        }
    }
}