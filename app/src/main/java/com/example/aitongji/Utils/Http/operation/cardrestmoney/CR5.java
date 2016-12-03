package com.example.aitongji.Utils.Http.operation.cardrestmoney;

import android.util.Log;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ResourceManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/22.
 */
public class CR5 extends Operation {
    private static final String CARD_INFO = "http://urp.tongji.edu.cn/index.portal?.pn=p84_p468_p469";

    public CR5(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
    }

//    public CR5(List<Operation> operations) {
//        super(operations);
//    }

    @Override
    public void perform() {
        NetWorkManager.getInstance().getCardRestHttpClient()
                .get(CARD_INFO, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        CharSequence card_info_raw = Jsoup.parse(new String(responseBody))
                                .body().getElementsByClass("portletContent").html();
                        Pattern pattern = Pattern.compile("<td>(.+?)</td>");
                        Matcher matcher = pattern.matcher(card_info_raw);
                        int cnt = 0;
                        while (matcher.find()) {
                            if (cnt == 12) {
                                ResourceManager.getInstance().setCardRest(matcher.group(1));
                                defaultSuccessCallBack.onSuccess(this.getClass());
                            }
                            cnt++;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e(getClass().getName(), "Failed!");
                        defaultFailCallBack.onFailure(this.getClass());
                    }
                });
    }
}
