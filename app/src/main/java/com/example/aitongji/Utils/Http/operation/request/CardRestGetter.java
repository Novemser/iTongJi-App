package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Factory.CardRestFactory;
import com.example.aitongji.Utils.Factory.RequestFactory;
import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;

/**
 * Project: AiTongji
 * Time: 2016/11/21 20:52
 * Package: ${PACKAGE_NAME}
 * Author: Novemser.
 */
public class CardRestGetter implements INetResourceGetter {
    @Override
    public void loadData(SuccessCallBack successCallBack, FailCallBack failCallBack) {
        NetWorkManager.getInstance().resetCardRestHttpClient();
        RequestFactory requestFactory = new CardRestFactory(successCallBack, failCallBack);
        requestFactory.createRequest();
        requestFactory.perform();
    }

    @Override
    public void refresh() throws Exception {

    }
}
