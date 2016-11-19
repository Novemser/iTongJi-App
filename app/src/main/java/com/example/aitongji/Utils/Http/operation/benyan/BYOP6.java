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
public class BYOP6 extends Operation {
    public BYOP6(List<Operation> operations) {
        super(operations);
    }
    private final String step1 = "http://4m3.tongji.edu.cn/eams/samlCheck";

    @Override
    public void perform() {
        NetWorkManager.getInstance().getBenyanHttpClient()
                .get(step1, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Document doc = Jsoup.parse(new String(responseBody));
                        String step2 = doc.select("meta").attr("content").substring(6);
                        setNextUrl(step2);
                        stepToNext();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
}
