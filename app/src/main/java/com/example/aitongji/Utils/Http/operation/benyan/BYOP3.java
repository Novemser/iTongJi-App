package com.example.aitongji.Utils.Http.operation.benyan;

import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/19.
 */
public class BYOP3 extends Operation {
    public BYOP3(List<Operation> operations) {
        super(operations);
    }

    @Override
    public void perform() {
        NetWorkManager.getInstance().getBenyanHttpClient()
                .get(getNextUrl(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Document doc = Jsoup.parse(new String(responseBody));
                        String step4 = doc.select("form").attr("action");
                        setNextUrl(step4);
                        stepToNext();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
}
