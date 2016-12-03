package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;

/**
 * Created by Novemser on 2016/11/14.
 */
public interface INetResourceGetter {
    void loadData(SuccessCallBack successCallBack, FailCallBack failCallBack) throws Exception;

    void refresh() throws Exception;
}
