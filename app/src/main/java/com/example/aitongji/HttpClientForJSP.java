package com.example.aitongji;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;

public class HttpClientForJSP {
    public String URL_BASE = "http://4m3.tongji.edu.cn/eams/";
    public static final String URL_CheckImage = "CheckImage";
    public static final String URL_MAIN = "home.action";
    public static final String URL_LOGIN = "login.action";
    public static final String imageFileName = "./CheckImage.jpg";

    private static HttpClient client = new DefaultHttpClient();

    public static StringBuffer cookie = null;

    public static File imageFile;

    public HttpClientForJSP() {

    }

    public void load() {
        HttpGet get = new HttpGet(URL_BASE);
        cookie = new StringBuffer();
        try {
            client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


