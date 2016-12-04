package com.example.aitongji.Utils.Http.operation.xuanke;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;

import java.util.List;

/**
 * Project: AiTongji
 * Time: 2016/12/3 21:26
 * Package: com.example.aitongji.Utils.Http.operation.xuanke
 * Author: Novemser.
 */
public abstract class XuankeOperation extends Operation {
    public static final String MSG_AUTH_INCORRECT = "用户名或密码好像不对哦";
    public static final String MSG_CAPTCHA_ERROR = "验证码识别失败";

    public XuankeOperation(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
    }


}
