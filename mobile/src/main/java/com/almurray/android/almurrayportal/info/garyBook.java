package com.almurray.android.almurrayportal.info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.almurray.android.almurrayportal.R;
import com.folioreader.util.FolioReader;

public class garyBook extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gary_book);

        web = findViewById(R.id.garyWeb);

        web.loadUrl("http://www.almurray.online/gary/bib/i/?book=gary.epub");
        web.getSettings().setJavaScriptEnabled(true);



        web.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
