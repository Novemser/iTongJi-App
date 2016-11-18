package com.example.aitongji.Utils.Http;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Novemser on 2/22/2016.
 */
public class AnnouncementReq4m3 {
    private Map<String, String> cookies;
    private static final String loginUrl_v2 = "http://tjis.tongji.edu.cn:58080/amserver/UI/Login";

    public AnnouncementReq4m3(final String username, final String password, final String info_id, final SuccessCallback successCallback, final FailureCallback failureCallback) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                Map<String, String> postValue = new HashMap<>();
                postValue.put("goto", "http://4m3.tongji.edu.cn:80/eams/home.action");
                postValue.put("gotoOnFail", "http://4m3.tongji.edu.cn:80/eams/login.action?error=1&username=333&password=37693cfc748049e45d87b8c7d8b9aacd");
                postValue.put("Login.Token1", username);
                postValue.put("Login.Token2", password);
                postValue.put("session_locale", "zh_CN");
                postValue.put("submitBtn", "登陆");

                Connection.Response response;
                try {
                    response = Jsoup.connect(loginUrl_v2).method(Connection.Method.POST).data(postValue).timeout(10000).execute();
                    // 登陆后保存cookies
                    cookies = response.cookies();
                    String INFO_BY_ID = "http://4m3.tongji.edu.cn/eams/noticeDocument!info.action?ifMain=1&notice.id=";
                    System.out.println();
                    Connection connect = Jsoup.connect(INFO_BY_ID + info_id);

                    for (Map.Entry<String, String> entry : cookies.entrySet()) {
                        connect.cookie(entry.getKey(), entry.getValue());
                    }

                    response = connect.method(Connection.Method.GET).timeout(10000).execute();
                    return response.parse().body().html().replaceAll("src=\"/eams", "src=\"http://4m3.tongji.edu.cn/eams");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String str) {
                super.onPostExecute(str);
                if (str != null) {
                    successCallback.onSuccess(str);
                } else {
                    failureCallback.onFailure();
                }
            }
        }.execute();
    }

    public interface SuccessCallback {
        void onSuccess(String str);
    }

    public interface FailureCallback {
        void onFailure();
    }

}
