package com.almurray.android.almurrayportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class complexPrayer extends AppCompatActivity {

    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_prayer);

        webView = findViewById(R.id.complexPrayerForm);
        webView.loadUrl("https://goo.gl/forms/fbRQYPYtLaxteyms1");
        webView.getSettings().setJavaScriptEnabled(true);
        final amigoLoanForm activity = new amigoLoanForm();

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
