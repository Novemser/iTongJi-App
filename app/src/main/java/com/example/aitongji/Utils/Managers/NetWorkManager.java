package com.example.aitongji.Utils.Managers;

import com.example.aitongji.Utils.Http.operation.request.BYCourseTableGetter;
import com.example.aitongji.Utils.Http.operation.request.BYGenericGetter;
import com.example.aitongji.Utils.Http.operation.request.BYTimeNotificationGetter;
import com.example.aitongji.Utils.Http.operation.request.CardRestGetter;
import com.example.aitongji.Utils.Http.operation.request.CookieGetter;
import com.example.aitongji.Utils.Http.operation.request.GPAGetter;
import com.loopj.android.http.SyncHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Novemser on 2016/11/14.
 */
public class NetWorkManager {
    private static NetWorkManager manager;

    public SyncHttpClient getCardRestHttpClient() {
        return cardRestHttpClient;
    }

    private SyncHttpClient cardRestHttpClient;

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

//    private Semaphore semaphore;

//    private static final int networkThreadCnt = 5;

    private NetWorkManager() {
        xuankeHttpClient = new SyncHttpClient();
        benyanHttpClient = new SyncHttpClient();
        cardRestHttpClient = new SyncHttpClient();
        cookies4m3 = new HashMap<>();
//        semaphore = new Semaphore(networkThreadCnt, true);
    }

    public void resetBenYanHttpClient() {
        benyanHttpClient = new SyncHttpClient();
    }

    public void resetXuankeHttpClient() {
        xuankeHttpClient = new SyncHttpClient();
    }

    public void resetCardRestHttpClient() {
        cardRestHttpClient = new SyncHttpClient();
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

        getGPAThenNotify();

        get4m3ThenNotify();

        getCardRestThenNotify();
    }

    public void getCardRestThenNotify() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new CardRestGetter().loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void get4m3ThenNotify() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;

                CookieGetter cookieGetter = new CookieGetter();
                while (count < 10) {
                    try {
                        cookieGetter.loadData();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        count++;
                    }
                }
                if (count == 10)
                    return;

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

    public void getGPAThenNotify() {
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
    }

    public void refreshAllData() {

    }
}
