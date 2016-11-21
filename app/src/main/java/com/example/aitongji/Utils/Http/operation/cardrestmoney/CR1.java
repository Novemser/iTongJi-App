package com.example.aitongji.Utils.Http.operation.cardrestmoney;

import android.os.Looper;

import com.example.aitongji.Utils.AndroidResource;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ResourceManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.jsoup.Jsoup;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/21.
 */
public class CR1 extends Operation {
    private SyncHttpClient syncHttpClient;
    private static final String CARD_LOGIN_1 = "http://urp.tongji.edu.cn/";
    private static final String CARD_LOGIN_2 = "http://urp.tongji.edu.cn/index.portal";
    private static final String CARD_LOGIN = "https://ids.tongji.edu.cn:8443/nidp/idff/sso?sid=0&sid=0";
    private static final String CARD_LOGIN_3 = "https://ids.tongji.edu.cn:8443/nidp/idff/sso?sid=0";
    private static final String CARD_INFO = "http://urp.tongji.edu.cn/index.portal?.pn=p84_p468_p469";

    public CR1(List<Operation> operations) {
        super(operations);
        syncHttpClient = NetWorkManager.getInstance().getCardRestHttpClient();

    }

    @Override
    public void perform() {
        // 设置Cookie容器，每次请求都需要清除以前保留的cookie
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(AndroidResource.getContext());
        persistentCookieStore.clear();
        syncHttpClient.setCookieStore(persistentCookieStore);
        syncHttpClient.get(CARD_LOGIN_1, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                syncHttpClient.get(CARD_LOGIN_2, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        RequestParams params = new RequestParams();
                        params.put("option", "credential");
                        params.put("target", "http://urp.tongji.edu.cn/index.portal");
                        params.put("Ecom_User_ID", ResourceManager.getInstance().getUserName());
                        params.put("Ecom_Password", ResourceManager.getInstance().getUserPwd());
                        params.put("submit", "登陆");

                        syncHttpClient.post(CARD_LOGIN, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                syncHttpClient.get(CARD_LOGIN_3, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        syncHttpClient.get(CARD_INFO, new AsyncHttpResponseHandler() {
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
                                                    }
                                                    cnt++;
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    }
                                });

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });

    }
}
