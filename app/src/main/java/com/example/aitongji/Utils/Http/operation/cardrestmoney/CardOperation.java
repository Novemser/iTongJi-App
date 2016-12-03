package com.example.aitongji.Utils.Http.operation.cardrestmoney;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;

import java.util.List;

/**
 * Project: AiTongji
 * Time: 2016/12/3 20:55
 * Package: com.example.aitongji.Utils.Http.operation.cardrestmoney
 * Author: Novemser.
 */
public abstract class CardOperation extends Operation {
    public final static String FAIL_MSG = "CardRestFailure";
    public CardOperation(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
    }
}
