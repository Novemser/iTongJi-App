package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR1;
import com.example.aitongji.Utils.Managers.NetWorkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novemser on 2016/11/21.
 */
public class CardRestGetter implements INetResourceGetter {
    @Override
    public void loadData() throws Exception {
        NetWorkManager.getInstance().resetCardRestHttpClient();
        List<Operation> operations = new ArrayList<>();
        operations.add(new CR1(operations));
        operations.remove(0).perform();
    }

    @Override
    public void refresh() throws Exception {

    }
}
