package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Factory.GPARequestFactory;
import com.example.aitongji.Utils.Factory.RequestFactory;
import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;

/**
 * Project: AiTongji
 * Time: 2016/11/19 20:51
 * Package: ${PACKAGE_NAME}
 * Author: Novemser.
 */
public class GPAGetter implements INetResourceGetter {

    @Override
    public synchronized void loadData(SuccessCallBack successCallBack, FailCallBack failCallBack) {
        NetWorkManager.getInstance().resetXuankeHttpClient();
        RequestFactory requestFactory = new GPARequestFactory(successCallBack, failCallBack);
        requestFactory.createRequest();
        requestFactory.perform();
    }

    @Override
    public void refresh() throws Exception {

    }
}
