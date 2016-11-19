package com.example.aitongji.Utils.Http.callback;

import com.example.aitongji.Utils.Managers.ResourceManager;

import java.util.List;

/**
 * Created by Novemser on 2016/11/18.
 */
public abstract class Operation {
    protected ResourceManager manager = ResourceManager.getInstance();
    protected List<Operation> operations;
    protected String nextUrl;

    public String getNextUrl() {
        return nextUrl;
    }

    public void setUrl(String url) {
        nextUrl = url;
    }

    public void setNextUrl(String nextUrl) {
        if (!operations.isEmpty())
            operations.get(0).setUrl(nextUrl);
    }


    public Operation(List<Operation> operations) {
        this.operations = operations;
    }

    protected void stepToNext() {
        if (!operations.isEmpty())
            operations.remove(0).perform();
    }

    public abstract void perform();
}
