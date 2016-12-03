package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR1;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR2;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR3;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR4;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR5;
import com.example.aitongji.Utils.Managers.NetWorkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novemser on 2016/11/21.
 */
public class CardRestGetter implements INetResourceGetter {
    @Override
    public void loadData(SuccessCallBack successCallBack, FailCallBack failCallBack) throws Exception {
        NetWorkManager.getInstance().resetCardRestHttpClient();
        List<Operation> operations = new ArrayList<>();
        operations.add(new CR1(operations, successCallBack, failCallBack));
        operations.add(new CR2(operations, successCallBack, failCallBack));
        operations.add(new CR3(operations, successCallBack, failCallBack));
        operations.add(new CR4(operations, successCallBack, failCallBack));
        operations.add(new CR5(operations, successCallBack, failCallBack));
        operations.remove(0).perform();
    }

    @Override
    public void refresh() throws Exception {

    }
}
