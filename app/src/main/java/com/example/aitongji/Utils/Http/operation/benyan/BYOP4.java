package com.example.aitongji.Utils.Http.operation.benyan;

import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/19.
 */
public class BYOP4 extends Operation {
    public BYOP4(List<Operation> operations) {
        super(operations);
    }

    @Override
    public void perform() {
        RequestParams postData = new RequestParams();
        postData.put("option", "credential");
        postData.put("Ecom_User_ID", manager.getUserName());
        postData.put("Ecom_Password", manager.getUserPwd());
        postData.put("submit", "登录");
        NetWorkManager.getInstance().getBenyanHttpClient()
                .post(getNextUrl(), postData, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Pattern pattern = Pattern.compile("top.location.href=\'(.*?)\'");
                        Matcher matcher = pattern.matcher(new String(responseBody));
                        if (matcher.find()) {
                            String step5 = matcher.group(1);
                            setNextUrl(step5);
                            stepToNext();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

    }
}
