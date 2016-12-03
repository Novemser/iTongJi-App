package com.example.aitongji.Utils.Factory;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: AiTongji
 * Time: 2016/12/3 20:50
 * Package: ${PACKAGE_NAME}
 * Author: Novemser.
 */
public abstract class RequestFactory {
    protected List<Operation> operations = new ArrayList<>();
    protected SuccessCallBack successCallBack;
    protected FailCallBack failCallBack;

    public RequestFactory(SuccessCallBack successCallBack, FailCallBack failCallBack) {
        this.successCallBack = successCallBack;
        this.failCallBack = failCallBack;
    }

    public abstract void createRequest();

    public void perform() {
        if (!operations.isEmpty())
            operations.remove(0).perform();
    }

}
