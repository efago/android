package com.efrem.halyot;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle( getIntent().getStringExtra("actionBarTitle"));
        webView= findViewById(R.id.browser);
        webView.setWebViewClient( new WebViewClient());
        webView.loadUrl( getIntent().getStringExtra("url"));
        WebSettings webSettings= webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else
            super.onBackPressed();
    }
}
