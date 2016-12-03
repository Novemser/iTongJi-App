package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Factory.GPARequestFactory;
import com.example.aitongji.Utils.Factory.RequestFactory;
import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;

/**
 * Created by Novemser on 2016/11/19.
 */
public class GPAGetter implements INetResourceGetter {

    @Override
    public synchronized void loadData(SuccessCallBack successCallBack, FailCallBack failCallBack) throws Exception {
        NetWorkManager.getInstance().resetXuankeHttpClient();
        RequestFactory requestFactory = new GPARequestFactory(successCallBack, failCallBack);
        requestFactory.createRequest();
        requestFactory.perform();
//        List<Operation> operations = new ArrayList<>();
//        operations.add(new XKOP1(operations, successCallBack, failCallBack));
//        operations.add(new XKOP2(operations, successCallBack, failCallBack));
//        operations.add(new XKOP3(operations, successCallBack, failCallBack));
//        operations.add(new XKOP4(operations, successCallBack, failCallBack));
//        operations.add(new XKOP5(operations, successCallBack, failCallBack));
//        operations.remove(0).perform();
    }

    @Override
    public void refresh() throws Exception {

    }
}
