package com.example.aitongji.Utils.Factory;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP1;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP2;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP3;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP4;
import com.example.aitongji.Utils.Http.operation.xuanke.XKOP5;

/**
 * Created by Novemser on 2016/12/3.
 */
public class GPARequestFactory extends RequestFactory {

    public GPARequestFactory(SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(successCallBack, failCallBack);
    }

    @Override
    public void createRequest() {
        operations.add(new XKOP1(operations, successCallBack, failCallBack));
        operations.add(new XKOP2(operations, successCallBack, failCallBack));
        operations.add(new XKOP3(operations, successCallBack, failCallBack));
        operations.add(new XKOP4(operations, successCallBack, failCallBack));
        operations.add(new XKOP5(operations, successCallBack, failCallBack));
    }
}
