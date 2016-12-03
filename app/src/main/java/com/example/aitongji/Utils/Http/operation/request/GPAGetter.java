package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP1;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP2;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP3;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP4;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP5;
import com.example.aitongji.Utils.Managers.NetWorkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novemser on 2016/11/19.
 */
public class GPAGetter implements INetResourceGetter {

    @Override
    public synchronized void loadData(SuccessCallBack successCallBack, FailCallBack failCallBack) throws Exception {
        NetWorkManager.getInstance().resetXuankeHttpClient();
        List<Operation> operations = new ArrayList<>();
        operations.add(new XKOP1(operations, successCallBack, failCallBack));
        operations.add(new XKOP2(operations, successCallBack, failCallBack));
        operations.add(new XKOP3(operations, successCallBack, failCallBack));
        operations.add(new XKOP4(operations, successCallBack, failCallBack));
        operations.add(new XKOP5(operations, successCallBack, failCallBack));
        operations.remove(0).perform();
    }

    @Override
    public void refresh() throws Exception {

    }
}
