package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Novemser on 2016/11/30.
 */
public class NewsContentGetter implements INetResourceGetter {
    protected Map<String, String> cookies = NetWorkManager.getInstance().getCookies4m3();
    private String info_id;
    private String content;

    public NewsContentGetter(String infoId) {
        info_id = infoId;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void loadData(SuccessCallBack successCallBack, FailCallBack failCallBack) {
        Connection.Response response;
        try {
            String INFO_BY_ID = "http://4m3.tongji.edu.cn/eams/noticeDocument!info.action?ifMain=1&notice.id=";
            System.out.println();
            Connection connect = Jsoup.connect(INFO_BY_ID + info_id);

            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                connect.cookie(entry.getKey(), entry.getValue());
            }

            response = connect.method(Connection.Method.GET).timeout(10000).execute();
            content = response.parse().body().html().replaceAll("src=\"/eams", "src=\"http://4m3.tongji.edu.cn/eams");
            successCallBack.onSuccess(getContent());
        } catch (IOException e) {
            e.printStackTrace();
            failCallBack.onFailure(null);
        }
    }

    @Override
    public void refresh() {

    }
}
