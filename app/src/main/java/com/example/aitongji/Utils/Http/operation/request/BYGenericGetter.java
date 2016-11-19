package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Managers.NetWorkManager;

import java.util.Map;

/**
 * Created by Novemser on 2016/11/19.
 */
public abstract class BYGenericGetter implements INetResourceGetter {
    protected Map<String, String> cookies = NetWorkManager.getInstance().getCookies4m3();
    protected final String homeAction = "http://4m3.tongji.edu.cn/eams/home!welcome.action";
}
