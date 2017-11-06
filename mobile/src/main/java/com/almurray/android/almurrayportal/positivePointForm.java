package com.almurray.android.almurrayportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class positivePointForm extends AppCompatActivity {


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positive_point_form);

        webView = findViewById(R.id.positiveForm);
        webView.loadUrl("https://goo.gl/forms/IKAxCiHbbYy963S72");
        webView.getSettings().setJavaScriptEnabled(true);


        final positivePointForm activity = new positivePointForm();

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
