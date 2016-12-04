package com.example.aitongji.Utils.Managers;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

import com.example.aitongji.Utils.observable.Observer;

/**
 * Created by Novemser on 2016/11/21.
 */
public class ObserverManager implements Observer {
    private Handler mHandler;


    private static ObserverManager manager;

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    private RecyclerView.Adapter adapter;

    private ObserverManager() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Integer row = (Integer) msg.obj;
                if (null != adapter) {
                    adapter.notifyItemChanged(row);
                }
            }
        };

        // 注册Data
        ResourceManager.getInstance().getNewsTitleSubject().registerObserver(this);
        ResourceManager.getInstance().getCourseTableSubject().registerObserver(this);
        ResourceManager.getInstance().getRestMoneySubject().registerObserver(this);
    }

    public static ObserverManager getInstance() {
        if (null == manager)
            synchronized (new Object()) {
                if (null == manager)
                    manager = new ObserverManager();
            }

        return manager;
    }

    /**
     * 通知某一行数据发生了变更
     *
     * @param row 行号 从0开始
     */
    public void notifyRowChanged(int row) {
        if (null != adapter) {
            Message updateMessage = mHandler.obtainMessage(row, row);
            updateMessage.sendToTarget();
        }
    }

    @Override
    public void update(int rowNumber) {
        notifyRowChanged(rowNumber);
    }
}
