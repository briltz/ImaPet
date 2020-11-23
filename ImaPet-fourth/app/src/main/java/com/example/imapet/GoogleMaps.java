package com.example.imapet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GoogleMaps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        String location = getIntent().getStringExtra("LOCATION");

        String url = "https://www.google.ca/maps/place/" + location;

        WebView webView = (WebView)findViewById(R.id.website_detail);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}