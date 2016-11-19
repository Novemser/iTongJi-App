package com.example.aitongji.Utils.Http.operation.benyan;

import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Managers.NetWorkManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Novemser on 2016/11/19.
 */
public class BYOperation extends Operation {
    protected Map<String, String> cookies = NetWorkManager.getInstance().getCookies4m3();

    public BYOperation(List<Operation> operations) {
        super(operations);
    }

    @Override
    public void perform() {

    }
}
