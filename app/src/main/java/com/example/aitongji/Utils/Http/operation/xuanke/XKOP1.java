package com.example.aitongji.Utils.Http.operation.xuanke;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Looper;
import android.util.Log;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/18.
 */
public class XKOP1 extends Operation {
    private final static String CHECK_IMAGE = "http://xuanke.tongji.edu.cn/CheckImage";

    private SyncHttpClient syncHttpClient;

    public XKOP1(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
        syncHttpClient = NetWorkManager.getInstance().getXuanKeHttpClient();
    }

    @Override
    public void perform() {
        manager.setPieces(new ArrayList<Bitmap>());

        // 设置Cookie容器，每次请求都需要清除以前保留的cookie
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(manager.getApplicationContext());
        persistentCookieStore.clear();
        syncHttpClient.setCookieStore(persistentCookieStore);

        syncHttpClient.get(CHECK_IMAGE, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 用神经网络识别验证码
                manager.setCheckCodeBm(BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length));

                if (manager.getCheckCodeBm() != null)
                    spiltCheckImage(manager.getCheckCodeBm());

                String result = "";
                for (Bitmap item : manager.getPieces()) {
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
                    double[] res = manager.getBpNN().test(input);
                    result += getMax(res);
                }

                manager.setOcrResult(result);
                // 进行下一个操作
                stepToNext();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                manager.setGPATable(null);
                Log.e(getClass().getName(), "Failed!");
                defaultFailCallBack.onFailure(this.getClass());
            }
        });
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
        manager.getPieces().clear();
        for (int i = 0; i < 4; i++) {
            manager.getPieces().add(Bitmap.createBitmap(bitmap, 2 + i * 9, 3, 8, 12));
        }
    }
}
