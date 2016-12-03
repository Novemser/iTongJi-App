package com.example.aitongji.Utils.Factory;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR1;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR2;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR3;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR4;
import com.example.aitongji.Utils.Http.operation.cardrestmoney.CR5;

/**
 * Created by Novemser on 2016/12/3.
 */
public class CardRestFactory extends RequestFactory {
    public CardRestFactory(SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(successCallBack, failCallBack);
    }

    @Override
    public void createRequest() {
        operations.add(new CR1(operations, successCallBack, failCallBack));
        operations.add(new CR2(operations, successCallBack, failCallBack));
        operations.add(new CR3(operations, successCallBack, failCallBack));
        operations.add(new CR4(operations, successCallBack, failCallBack));
        operations.add(new CR5(operations, successCallBack, failCallBack));
    }
}
