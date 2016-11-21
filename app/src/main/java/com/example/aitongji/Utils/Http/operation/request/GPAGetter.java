package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Http.callback.Operation;
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
    public synchronized void loadData() throws Exception {
        NetWorkManager.getInstance().resetXuankeHttpClient();
        List<Operation> operations = new ArrayList<>();
        operations.add(new XKOP1(operations));
        operations.add(new XKOP2(operations));
        operations.add(new XKOP3(operations));
        operations.add(new XKOP4(operations));
        operations.add(new XKOP5(operations));
        operations.remove(0).perform();
    }

    @Override
    public void refresh() throws Exception {

    }
}
