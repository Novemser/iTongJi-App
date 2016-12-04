package com.example.aitongji.Utils.Http.operation.xuanke;

import android.util.Log;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/19.
 */
public class XKOP3 extends XuankeOperation {
    public XKOP3(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
    }

    @Override
    public void perform() {
        NetWorkManager.getInstance().getXuanKeHttpClient()
                .get(
                        "http://xuanke.tongji.edu.cn/pass.jsp?checkCode=" + manager.getOcrResult()
                        , new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // length 小于2000才是成功 否则密码错误(很tricky...)
                final int len = Jsoup.parse(new String(responseBody)).html().length();
                if (len < 2000) {
                    stepToNext();
                } else {
                    String sb = new String(responseBody);
                    if (sb.contains("ͳһ�����֤ʧ��,�����µ�¼!")) {
                        Log.e(getClass().getName(), "密码错误！");
                        defaultFailCallBack.onFailure(MSG_AUTH_INCORRECT);
                    }
                    else {
                        Log.e(getClass().getName(), "自动识别验证码错误！");
                        defaultFailCallBack.onFailure(MSG_CAPTCHA_ERROR);
                    }
                    manager.setGPATable(null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                manager.setGPATable(null);
                Log.e(getClass().getName(), "Failed!");
                defaultFailCallBack.onFailure(this.getClass());
            }
        });
    }
}
