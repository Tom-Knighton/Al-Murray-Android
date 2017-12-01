package com.almurray.android.almurrayportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class requestAccount extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_account);

        webView = findViewById(R.id.requestWebView);
        webView.loadUrl("https://goo.gl/forms/57CPZKo6LjzaKGhn2");
        webView.getSettings().setJavaScriptEnabled(true);
        final amigoLoanForm activity = new amigoLoanForm();

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
