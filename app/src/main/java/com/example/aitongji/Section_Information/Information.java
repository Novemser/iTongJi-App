package com.example.aitongji.Section_Information;

import android.app.Fragment;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.example.aitongji.R;

import java.util.ArrayList;

/**
 * Created by Novemser on 2015/9/26.
 */
public class Information extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String[]> data = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.section_one_infomation, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.id_section1_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(root.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (data.size() == 0)
        {
            data.add(new String[]{"选课网", "http://xuanke.tongji.edu.cn", "选课网教务通知", "选", "#5cd3f1"});
            data.add(new String[] {"4m3本研一体化", "http://4m3.tongji.edu.cn", "4m3教务通知", "本", "#5fe974"});
            data.add(new String[] {"软件学院通知中心", "http://sse.tongji.edu.cn", "软件学院通知", "软", "#ef87fa"});
            data.add(new String[] {"电信学院通知中心", "http://see.tongji.edu.cn", "电信学院通知", "电", "#fafa46"});
        }

        mAdapter = new Section_Info_Adapter(data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });

        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) root.findViewById(R.id.id_section1_pullRefreshLayout);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);

        return root;
    }
}
