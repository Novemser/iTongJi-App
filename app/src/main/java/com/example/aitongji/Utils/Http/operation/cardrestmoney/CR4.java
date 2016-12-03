package com.example.aitongji.Utils.Http.operation.cardrestmoney;

import android.util.Log;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/22.
 */
public class CR4 extends CardOperation {
    private static final String CARD_LOGIN_3 = "https://ids.tongji.edu.cn:8443/nidp/idff/sso?sid=0";

    public CR4(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
    }

    @Override
    public void perform() {
        NetWorkManager.getInstance().getCardRestHttpClient()
                .get(CARD_LOGIN_3, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        stepToNext();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e(getClass().getName(), "Failed!");
                        defaultFailCallBack.onFailure(FAIL_MSG);
                    }
                });
    }
}
