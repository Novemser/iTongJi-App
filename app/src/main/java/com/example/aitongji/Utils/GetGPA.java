package com.example.aitongji.Utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.aitongji.Home.MainActivity;
import com.example.aitongji.WelcomeSceneAty;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.novemser.ocrtest.BP;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

/**
 * Created by Novemser on 3/7/2016.
 */
public class GetGPA {
    private Map<String, String> cookies;
    private final static String MAIN = "http://xuanke.tongji.edu.cn";
    private final static String CHECK_IMAGE = "http://xuanke.tongji.edu.cn/CheckImage";
    private final static String URL_LOGIN = "http://tjis2.tongji.edu.cn:58080/amserver/UI/Login";
    private ArrayList<Bitmap> pieces;
    private String result;
    private BP bp;
    private OkHttpClient httpClient;
    private AsyncHttpClient client;


    public GetGPA() {
        new GetGPA("1452681", "a20131002", null, null);
    }

    public GetGPA(final String username, final String password, final SuccessCallback successCallback, final FailureCallback failureCallback) {

    }

    private int getMax(double[] input) {
        double tmp = -1;
        int j = 0;
        for (int i = 0; i < 10; i++) {
            if (input[i] > tmp) {
                tmp = input[i];
                j = i;
            }
        }
        return j;
    }

    private void spiltCheckImage(Bitmap bitmap) {
        pieces.clear();
        for (int i = 0; i < 4; i++) {
            pieces.add(Bitmap.createBitmap(bitmap, 2 + i * 9, 3, 8, 12));
        }
    }

    public interface SuccessCallback {
        void onSuccess(String data);
    }

    public interface FailureCallback {
        void onFailure();
    }
}
