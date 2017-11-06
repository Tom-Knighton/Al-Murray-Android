package com.almurray.android.almurrayportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class tipForm extends AppCompatActivity {


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_form);

        webView = findViewById(R.id.tipFormWeb);
        webView.loadUrl("https://goo.gl/forms/ipiOHwHxdPoxr4Od2");
        webView.getSettings().setJavaScriptEnabled(true);


        final tipForm activity = new tipForm();

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
