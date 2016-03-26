package com.example.aitongji.Section_Elect;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.aitongji.Base.ToolbarActivity;
import com.example.aitongji.R;
import com.jaredrummler.materialspinner.MaterialSpinner;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ElectricityQuery extends ToolbarActivity {
    //    @Bind(R.id.campus)
//    MaterialSpinner campus;
//    @Bind(R.id.building)
//    MaterialSpinner building;
    @Bind(R.id.webview)
    WebView myWebView;
    @Bind(R.id.progressbar)
    MaterialProgressBar progressBar;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_electricity_query;
    }

    private String[] da;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mToolbar.setTitle("电费查询");
        WebSettings webSettings = myWebView.getSettings();

        // Configure related browser settings
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        myWebView.requestFocusFromTouch();

        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        myWebView.setWebViewClient(new MyBrowser());
        // Load the initial URL
        myWebView.loadUrl("http://202.120.165.79:8800/Default.aspx");
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100)
                    progressBar.setVisibility(View.GONE);
                else
                    progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    // Manages the behavior when URLs are loaded
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
