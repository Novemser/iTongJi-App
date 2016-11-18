package com.example.aitongji.Utils.Factory.callback.xuanke;

import com.example.aitongji.Utils.Factory.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ResourceManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/19.
 */
public class XKOP2 extends Operation {
    private final static String URL_LOGIN = "http://tjis2.tongji.edu.cn:58080/amserver/UI/Login";

    public XKOP2(List<Operation> operations) {
        super(operations);
    }

    @Override
    public void perform() {
        RequestParams params = new RequestParams();
        params.put("Login.Token1", manager.getUserName());
        params.put("Login.Token2", manager.getUserPwd());
        params.put("T3", manager.getOcrResult());
        params.put("goto", "http://xuanke.tongji.edu.cn/pass.jsp?checkCode=" + manager.getOcrResult());
        params.put("gotoOnFail", "http://xuanke.tongji.edu.cn/deny.jsp?checkCode=" + manager.getOcrResult() + "&account=1452681&password=BED0648254DF3D9BA9350683817275CE");

        NetWorkManager.getInstance().getSyncHttpClient().post(URL_LOGIN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                manager.setGPATable(null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // 302到目标地址验证是否登陆成功
                if (!operations.isEmpty())
                    operations.remove(0).perform();
            }
        });
    }
}
