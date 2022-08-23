package com.example.mybook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.ybq.android.spinkit.SpinKitView;

public class webView extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        progressDialog = new ProgressDialog(webView.this);
        progressDialog.setMessage("Fetching correct details.....");
        progressDialog.setCancelable(false);


        toolbar = findViewById(R.id.webviewToolbar);
        setSupportActionBar(toolbar);

        webView = findViewById(R.id.newWebView);
        Intent intent = getIntent();
        progressDialog.show();
        String url = intent.getStringExtra("url");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        progressDialog.dismiss();
    }
}