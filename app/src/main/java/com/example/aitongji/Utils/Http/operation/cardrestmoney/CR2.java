package com.example.aitongji.Utils.Http.operation.cardrestmoney;

import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ResourceManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/22.
 */

public class CR2 extends Operation {
    private static final String CARD_LOGIN_2 = "http://urp.tongji.edu.cn/index.portal";

    public CR2(List<Operation> operations) {
        super(operations);
    }

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

                    }
                });
    }
}
