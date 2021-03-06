package com.example.aitongji.Utils.Managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Http.operation.request.BYCourseTableGetter;
import com.example.aitongji.Utils.Http.operation.request.BYGenericGetter;
import com.example.aitongji.Utils.Http.operation.request.BYTimeNotificationGetter;
import com.example.aitongji.Utils.Http.operation.request.CardRestGetter;
import com.example.aitongji.Utils.Http.operation.request.CookieGetter;
import com.example.aitongji.Utils.Http.operation.request.GPAGetter;
import com.example.aitongji.Utils.Http.operation.xuanke.XuankeOperation;
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

    private final int MAX_RETRY_TIME = 5;

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

    public void obtainAllDataThenNotify(View view) {
        if (!isNetworkAvailable()) {
            Snackbar.make(view, "网络连接似乎不太好 X﹏X", Snackbar.LENGTH_SHORT).setAction("知道啦", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
            return;
        }

        getGPAThenNotify(view);

        get4m3ThenNotify(view);

        getCardRestThenNotify(view);
    }

    private int cardRestTryCnt;

    public void getCardRestThenNotify(final View view) {
        new AsyncTask<View, Void, Void>() {

            @Override
            protected Void doInBackground(View... params) {
                new CardRestGetter().loadData(new SuccessCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.e(getClass().getName(), "加载余额成功！");
                        cardRestTryCnt = 0;
                    }
                }, new FailCallBack() {
                    @Override
                    public void onFailure(Object data) {
                        Log.e(getClass().getName(), "加载余额失败！");
                        if (cardRestTryCnt < MAX_RETRY_TIME) {
                            cardRestTryCnt++;
                            getCardRestThenNotify(view);
                        }
                    }
                });
                return null;
            }
        }.execute(view);
    }

    private int byCookieTryCnt;

    public void get4m3ThenNotify(final View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                CookieGetter cookieGetter = new CookieGetter();
                cookieGetter.loadData(new SuccessCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.e(getClass().getName(), "加载Cookie成功！");
                        byCookieTryCnt = 0;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                BYGenericGetter getter = new BYCourseTableGetter();
                                getter.loadData(new SuccessCallBack() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        Log.e(getClass().getName(), "加载Course成功！");

                                    }
                                }, new FailCallBack() {
                                    @Override
                                    public void onFailure(Object data) {
                                        Log.e(getClass().getName(), "加载Course失败！");
                                    }
                                });

                                getter = new BYTimeNotificationGetter();
                                getter.loadData(new SuccessCallBack() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        Log.e(getClass().getName(), "加载TimeNotification成功！");
                                    }
                                }, new FailCallBack() {
                                    @Override
                                    public void onFailure(Object data) {
                                        Log.e(getClass().getName(), "加载TimeNotification失败！");
                                    }
                                });
                            }
                        }).start();
                    }
                }, new FailCallBack() {
                    @Override
                    public void onFailure(Object data) {
                        Log.e(getClass().getName(), "加载Cookie失败！");
                        if (byCookieTryCnt < MAX_RETRY_TIME) {
                            byCookieTryCnt++;
                            get4m3ThenNotify(view);
                        }
                    }
                });

            }
        }).start();
    }

    private int GPATryCount;

    public void getGPAThenNotify(final View view) {
        final SuccessCallBack successCallBack = new SuccessCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.e(getClass().getName(), "加载GPA成功！");
                GPATryCount = 0;
            }
        };

        final FailCallBack failCallBack = new FailCallBack() {
            @Override
            public void onFailure(Object data) {
                String msg = data.toString();
                Log.e(getClass().getName(), "加载GPA失败！");
                Log.e(getClass().getName(), msg);
                // 用户身份认证失败
                if (msg.equals(XuankeOperation.MSG_AUTH_INCORRECT)) {
                    // 停止所有任务
                    GPATryCount = MAX_RETRY_TIME;
                    byCookieTryCnt = MAX_RETRY_TIME;
                    cardRestTryCnt = MAX_RETRY_TIME;
                    ResourceManager.getInstance().getMainAty().showMsg(XuankeOperation.MSG_AUTH_INCORRECT);
                    return;
                }
                // 验证码识别失败
                if (GPATryCount < MAX_RETRY_TIME) {
                    GPATryCount++;
                    getGPAThenNotify(view);
                }
            }
        };


        new AsyncTask<View, Integer, Void>() {
            @Override
            protected Void doInBackground(View... params) {
                GPAGetter gpaGetter = new GPAGetter();
                gpaGetter.loadData(successCallBack, failCallBack);
                publishProgress(0);
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }
        }.execute(view);
    }

    public void refreshAllData(View view) {
        obtainAllDataThenNotify(view);
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ResourceManager.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
