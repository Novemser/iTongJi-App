package com.example.aitongji.Utils.Managers;

import com.loopj.android.http.SyncHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Novemser on 2016/11/14.
 */
public class NetWorkManager {
    private static NetWorkManager manager;

    public SyncHttpClient getXuanKeHttpClient() {
        return xuankeHttpClient;
    }

    private SyncHttpClient xuankeHttpClient;

    public Map<String, String> getCookies4m3() {
        return cookies4m3;
    }

    private Map<String, String> cookies4m3;

    public SyncHttpClient getBenyanHttpClient() {
        return benyanHttpClient;
    }

    private SyncHttpClient benyanHttpClient;

    private NetWorkManager() {
        xuankeHttpClient = new SyncHttpClient();
        benyanHttpClient = new SyncHttpClient();
        cookies4m3 = new HashMap<>();
    }

    public void resetXuankeHttpClient() {
        xuankeHttpClient = new SyncHttpClient();
    }

    public void set4m3Cookie(String key, String value) {
        cookies4m3.put(key, value);
    }

    public static NetWorkManager getInstance() {

        if (null == manager)
            synchronized (new Object()) {
                if (null == manager) {
                    manager = new NetWorkManager();
                }
            }

        return manager;
    }

    public void obtainAllData() {

    }

    public void refreshAllData() {

    }
}
