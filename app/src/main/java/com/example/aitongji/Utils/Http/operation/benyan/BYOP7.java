package com.example.aitongji.Utils.Http.operation.benyan;

import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/19.
 */
public class BYOP7 extends Operation {
    public BYOP7(List<Operation> operations) {
        super(operations);
    }

    @Override
    public void perform() {
        NetWorkManager.getInstance().getBenyanHttpClient()
                .get(getNextUrl(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String b = new String(responseBody);
                        Pattern pattern = Pattern.compile("top.location.href=\'(.*?)\'");
                        Matcher matcher = pattern.matcher(new String(responseBody));
                        if (matcher.find()) {
                            String next = matcher.group(1);
                            setNextUrl(next);
                            stepToNext();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String b = new String(responseBody);
                    }
                });
    }
}
