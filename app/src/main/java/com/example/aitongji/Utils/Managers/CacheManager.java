package com.example.aitongji.Utils.Managers;

import android.os.AsyncTask;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Http.operation.request.NewsContentGetter;
import com.example.aitongji.Utils.cache.Info;
import com.example.aitongji.Utils.cache.NewsInfo;

import java.util.Hashtable;

/**
 * Created by Novemser on 2016/11/30.
 */
public class CacheManager {
    private static Hashtable<String, Info> infoMap
            = new Hashtable<>();

    public static Info getInfo(String infoId) {
        Info info = null;
        if (infoMap.containsKey(infoId))
            info = infoMap.get(infoId);

        if (null != info)
            return (Info) info.clone();
        return null;
    }

    public static void loadCache(String id, final SuccessCallBack successCallBack, final FailCallBack failCallBack) {
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                NewsContentGetter getter = new NewsContentGetter(params[0]);
                getter.loadData();
                Info info = new NewsInfo(params[0]);
                info.setContent(getter.getContent());
                infoMap.put(params[0], info);
                return getter.getContent();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (null != s && !s.equals("")) {
                    successCallBack.onSuccess(s);
                }
                else {
                    failCallBack.onFailure(s);
                }
            }
        }.execute(id);
    }
}
