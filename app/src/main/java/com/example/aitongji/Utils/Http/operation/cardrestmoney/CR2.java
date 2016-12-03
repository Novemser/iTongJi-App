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

public class CR2 extends Operation {
    private static final String CARD_LOGIN_2 = "http://urp.tongji.edu.cn/index.portal";

    public CR2(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
    }

//    public CR2(List<Operation> operations) {
//        super(operations);
//    }

    @Override
    public void perform() {
        NetWorkManager.getInstance().getCardRestHttpClient()
                .get(CARD_LOGIN_2, new AsyncHttpResponseHandler() {
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
