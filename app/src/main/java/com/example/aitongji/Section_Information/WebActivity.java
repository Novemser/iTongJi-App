package com.example.aitongji.Section_Information;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.aitongji.Base.BaseActivity;
import com.example.aitongji.R;
import com.example.aitongji.Utils.AnnouncementReq4m3;

public class WebActivity extends BaseActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);

        new AnnouncementReq4m3(intent.getStringExtra("username"), intent.getStringExtra("password"), intent.getStringArrayListExtra("info_id").get(intent.getIntExtra("infoId", 0)), new AnnouncementReq4m3.SuccessCallback() {
            @Override
            public void onSuccess(String str) {
                mWebView.loadData(str, "text/html; charset=UTF-8", null);
            }
        }, new AnnouncementReq4m3.FailureCallback() {
            @Override
            public void onFailure() {

            }
        });
    }

}
