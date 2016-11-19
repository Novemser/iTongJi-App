package com.example.aitongji.Utils.Http.operation.benyan;

import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/19.
 */
public class BYOP8 extends Operation {
    public BYOP8(List<Operation> operations) {
        super(operations);
    }

    @Override
    public void perform() {
        NetWorkManager.getInstance().getBenyanHttpClient()
                .get(getNextUrl(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String b = new String(responseBody);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
}
