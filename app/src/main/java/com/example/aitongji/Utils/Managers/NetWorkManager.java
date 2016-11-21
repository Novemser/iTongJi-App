package com.example.aitongji.Utils.Managers;

import com.example.aitongji.Utils.Http.operation.request.BYCourseTableGetter;
import com.example.aitongji.Utils.Http.operation.request.BYGenericGetter;
import com.example.aitongji.Utils.Http.operation.request.BYTimeNotificationGetter;
import com.example.aitongji.Utils.Http.operation.request.CookieGetter;
import com.example.aitongji.Utils.Http.operation.request.GPAGetter;
import com.loopj.android.http.SyncHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Novemser on 2016/11/14.
 */
public class NetWorkManager {
    // 定义一个线程池去进行网络请求
    protected ThreadPoolExecutor fixedThreadPool;

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
        // 最多5个线程...
        fixedThreadPool = new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
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

    public void obtainAllDataThenNotify() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                GPAGetter gpaGetter = new GPAGetter();
                try {
                    gpaGetter.loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 最多自动验证10次
                while (count < 10 && (null == ResourceManager.getInstance().getGPATable())) {
                    try {
                        gpaGetter.loadData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CookieGetter cookieGetter = new CookieGetter();
                try {
                    cookieGetter.loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BYGenericGetter getter = new BYCourseTableGetter();
                        try {
                            getter.loadData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        getter = new BYTimeNotificationGetter();
                        try {
                            getter.loadData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }).start();

    }

    public void refreshAllData() {

    }
}
