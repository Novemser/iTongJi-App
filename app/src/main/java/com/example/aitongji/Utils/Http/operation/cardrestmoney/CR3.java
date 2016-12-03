package com.example.aitongji.Utils.Http.operation.cardrestmoney;

import android.util.Log;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ResourceManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/22.
 */
public class CR3 extends Operation {
    private static final String CARD_LOGIN = "https://ids.tongji.edu.cn:8443/nidp/idff/sso?sid=0&sid=0";

    public CR3(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
    }

//    public CR3(List<Operation> operations) {
//        super(operations);
//    }

    @Override
    public void perform() {
        RequestParams params = new RequestParams();
        params.put("option", "credential");
        params.put("target", "http://urp.tongji.edu.cn/index.portal");
        params.put("Ecom_User_ID", ResourceManager.getInstance().getUserName());
        params.put("Ecom_Password", ResourceManager.getInstance().getUserPwd());
        params.put("submit", "登陆");
        NetWorkManager.getInstance().getCardRestHttpClient()
                .post(CARD_LOGIN, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        stepToNext();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e(getClass().getName(), "Failed!");
                        defaultFailCallBack.onFailure(getClass());
                    }
                });
    }
}
