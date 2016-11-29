package com.example.aitongji.Section_Information;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.aitongji.activity.BaseActivity;
import com.example.aitongji.R;

public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);

        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // handle different requests for different type of files
                // this example handles downloads requests for .apk and .mp3 files
                // everything else the webview can handle normally
                Log.d("Download", "Touch");
                if (url.endsWith(".doc")) {
                    Uri source = Uri.parse(url);
                    // Make a new request pointing to the .apk url
                    DownloadManager.Request request = new DownloadManager.Request(source);
                    // appears the same in Notification bar while downloading
                    request.setDescription("Description for the DownloadManager Bar");
                    request.setTitle("YourFile.doc");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    // save the file in the "Downloads" folder of SDCARD
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SmartPigs.doc");
                    // get download service and enqueue file
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                } else if (url.endsWith(".mp3")) {
                    // if the link points to an .mp3 resource do something else
                }
                // if there is a link to anything else than .apk or .mp3 load the URL in the webview
                else view.loadUrl(url);
                return true;
            }
        });

//        new AnnouncementReq4m3(intent.getStringExtra("username"), intent.getStringExtra("password"), intent.getStringArrayListExtra("info_id").get(intent.getIntExtra("infoId", 0)), new AnnouncementReq4m3.SuccessCallback() {
//            @Override
//            public void onSuccess(String str) {
////                Log.d("WebActivity", str);
//                mWebView.loadData(str, "text/html; charset=UTF-8", null);
//            }
//        }, new AnnouncementReq4m3.FailureCallback() {
//            @Override
//            public void onFailure() {
//
//            }
//        });
    }

}
