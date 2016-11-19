package com.example.aitongji.Utils.Http.operation.benyan;

import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/19.
 */
public class BYOP1 extends BYOperation {
    private final String step1 = "http://4m3.tongji.edu.cn/eams/samlCheck";
    private final String url = "http://4m3.tongji.edu.cn/eams/home!welcome.action";

    public BYOP1(List<Operation> operations) {
        super(operations);

    }

    @Override
    public void perform() {

    }
}
