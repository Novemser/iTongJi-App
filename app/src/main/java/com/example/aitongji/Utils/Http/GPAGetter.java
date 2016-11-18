package com.example.aitongji.Utils.Http;

import com.example.aitongji.Utils.Factory.Operation;
import com.example.aitongji.Utils.Factory.callback.xuanke.XKOP1;
import com.example.aitongji.Utils.Factory.callback.xuanke.XKOP2;
import com.example.aitongji.Utils.Factory.callback.xuanke.XKOP3;
import com.example.aitongji.Utils.Factory.callback.xuanke.XKOP4;
import com.example.aitongji.Utils.Factory.callback.xuanke.XKOP5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Novemser on 2016/11/19.
 */
public class GPAGetter implements INetResourceGetter {

    @Override
    public void loadData() throws Exception {
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
