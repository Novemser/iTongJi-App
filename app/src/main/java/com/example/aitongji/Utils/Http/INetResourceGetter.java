package com.example.aitongji.Utils.Http;

/**
 * Created by Novemser on 2016/11/14.
 */
public interface INetResourceGetter {
    void loadData() throws Exception;

    void refresh() throws Exception;
}
