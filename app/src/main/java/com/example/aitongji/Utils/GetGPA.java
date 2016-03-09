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

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                httpClient = new OkHttpClient();
                // 初始化数据
                Map<String, String> postValue = new HashMap<>();

                pieces = new ArrayList<>();
                result = "";
                // 从Assets文件夹读取BP神经网络
                AssetManager assetManager = MainActivity.getContext().getAssets();
                try {
                    ObjectInputStream ois = new ObjectInputStream(assetManager.open("bp.dat"));
                    bp = (BP) ois.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Connection.Response response;
                try {
                    Request req = new Request.Builder().url(CHECK_IMAGE).build();
                    Response resp = httpClient.newCall(req).execute();
                    byte[] bytes = resp.body().bytes();
                    CookieJar cookieJar = httpClient.cookieJar();
                    System.out.println(cookieJar.toString());
                    // 获取验证码
//                    response = Jsoup.connect(CHECK_IMAGE).method(Connection.Method.GET).timeout(10000).execute();
//                    cookies = response.cookies();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    if (bitmap != null) {
                        spiltCheckImage(bitmap);
                    }

                    for (Bitmap item : pieces) {
                        double[] input = new double[96];
                        int cnt = 0;
                        for (int i = item.getHeight() - 1; i >= 0; i--)
                            for (int j = item.getWidth() - 1; j >= 0; j--) {
                                int R = Color.red(item.getPixel(j, i));
                                if (R > 200) {
                                    input[cnt++] = 1.0;
                                } else {
                                    input[cnt++] = 0.0;
                                }
                            }
                        double[] res = bp.test(input);
                        result += getMax(res);
                    }
                    System.out.println(result);

                    postValue.put("goto", "http://xuanke.tongji.edu.cn/pass.jsp?checkCode=" + result);
                    postValue.put("gotoOnFail", "http://xuanke.tongji.edu.cn/deny.jsp?checkCode=" + result + "&account=1452681&password=BED0648254DF3D9BA9350683817275CE");
                    postValue.put("Login.Token1", username);
                    postValue.put("Login.Token2", password);
                    postValue.put("T3", result);


                    FormBody requestBody = new FormBody.Builder().add("Login.Token1", username).
                            add("Login.Token2", password).
                            add("T3", result).
                            add("goto", "http://xuanke.tongji.edu.cn/pass.jsp?checkCode=" + result).
                            add("gotoOnFail", "http://xuanke.tongji.edu.cn/deny.jsp?checkCode=" + result + "&account=1452681&password=BED0648254DF3D9BA9350683817275CE").
                            build();

                    req = new Request.Builder().url(URL_LOGIN).post(requestBody).build();
                    resp = httpClient.newCall(req).execute();
                    System.out.println(Jsoup.parse(resp.body().string()).html());
                    System.out.println(resp.code());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
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
