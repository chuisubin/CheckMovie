package com.example.checkmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Cinemas extends AppCompatActivity {
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinemas);
        web=(WebView)findViewById(R.id.cinemas_web);
        web.loadUrl("https://movie.yahoo-leisure.hk/cinema?url=cinema");
        web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }
}
