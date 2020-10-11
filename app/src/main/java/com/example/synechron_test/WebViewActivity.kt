package com.example.synechron_test


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.webViewClient = WebViewClient()
        if (intent != null) {
            val url = intent?.getStringExtra(CONSTANT.CONTENT_URL)
            url?.let { myWebView.loadUrl(it) }

        }
    }

}