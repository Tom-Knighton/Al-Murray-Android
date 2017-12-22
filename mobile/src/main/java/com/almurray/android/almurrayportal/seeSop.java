package com.almurray.android.almurrayportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class seeSop extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_sop);

        webView = findViewById(R.id.sopWeb);
        webView.loadUrl("https://docs.google.com/document/d/1idi5dugfa5mCzslcTcciGDO4O_QEwlNkG0WZLKrIyy0/edit?usp=sharing");
        webView.getSettings().setJavaScriptEnabled(true);
        final amigoLoanForm activity = new amigoLoanForm();

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
