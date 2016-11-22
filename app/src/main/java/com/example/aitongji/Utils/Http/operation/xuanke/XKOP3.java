package com.example.aitongji.Utils.Http.operation.xuanke;

import android.content.Intent;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aitongji.Home.MainActivity;
import com.example.aitongji.Section_Elect.ElectricityQuery;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ResourceManager;
import com.example.aitongji.WelcomeSceneAty;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.EncodingUtils;

/**
 * Created by Novemser on 2016/11/19.
 */
public class XKOP3 extends Operation {

    public XKOP3(List<Operation> operations) {
        super(operations);
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
                        Log.w(getClass().getName(), "密码错误！");
                    }
                    else {
                        Log.w(getClass().getName(), "自动识别验证码错误！");
                    }
                    manager.setGPATable(null);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                manager.setGPATable(null);
            }
        });
    }
}
