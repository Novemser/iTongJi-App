package com.example.aitongji.Utils.Managers;

import com.loopj.android.http.SyncHttpClient;

/**
 * Created by Novemser on 2016/11/14.
 */
public class NetWorkManager {
    private static NetWorkManager manager;

    public SyncHttpClient getSyncHttpClient() {
        return syncHttpClient;
    }

    private SyncHttpClient syncHttpClient;

    private NetWorkManager() {
        syncHttpClient = new SyncHttpClient();
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
