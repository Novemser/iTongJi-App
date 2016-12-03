package com.example.aitongji.Utils.Http.operation.cardrestmoney;

import android.os.Looper;
import android.util.Log;

import com.example.aitongji.Utils.AndroidResource;
import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/21.
 */
public class CR1 extends Operation {
    private SyncHttpClient syncHttpClient;
    private static final String CARD_LOGIN_1 = "http://urp.tongji.edu.cn/";

    public CR1(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
        syncHttpClient = NetWorkManager.getInstance().getCardRestHttpClient();
    }

    @Override
    public void perform() {
        // 设置Cookie容器，每次请求都需要清除以前保留的cookie
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(AndroidResource.getContext());
        persistentCookieStore.clear();
        syncHttpClient.setMaxRetriesAndTimeout(5, 1000);
        syncHttpClient.setCookieStore(persistentCookieStore);
        syncHttpClient.get(CARD_LOGIN_1, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                stepToNext();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(getClass().getName(), "Failed!");
                defaultFailCallBack.onFailure(this.getClass());
            }
        });

    }
}
