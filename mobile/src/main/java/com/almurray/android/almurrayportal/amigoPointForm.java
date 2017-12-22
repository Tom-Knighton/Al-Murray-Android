package com.almurray.android.almurrayportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class amigoPointForm extends AppCompatActivity {


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigo_point_form2);

        webView = findViewById(R.id.amigoPFormWeb);
        webView.loadUrl("https://almurray.typeform.com/to/mjKZRg");
        webView.getSettings().setJavaScriptEnabled(true);


        final amigoPointForm activity = new amigoPointForm();

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });



    }
}
