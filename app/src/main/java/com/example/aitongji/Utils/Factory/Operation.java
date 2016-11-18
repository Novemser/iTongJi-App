package com.example.aitongji.Utils.Factory;

import com.example.aitongji.Utils.Managers.ResourceManager;

import java.util.List;

/**
 * Created by Novemser on 2016/11/18.
 */
public abstract class Operation {
    protected ResourceManager manager = ResourceManager.getInstance();
    protected List<Operation> operations;

    public Operation(List<Operation> operations) {
        this.operations = operations;
    }

    public abstract void perform();
}
