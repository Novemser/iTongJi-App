package com.example.aitongji.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.aitongji.R;
import com.example.aitongji.Utils.CourseTable;
import com.example.aitongji.Utils.DataBundle;
import com.example.aitongji.Utils.DataHandler;

import java.util.ArrayList;

/**
 * Created by Novemser on 2016/2/11.
 */
public class HomePageCards extends Fragment {
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);
        setBundle((DataBundle) DataHandler.getObject(MainActivity.getContext(), "dataBundle.dat"));

        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.id_home_recyclerView);
        RecyclerView.Adapter mAdapter = new Home_Recycler_Adapter(bundle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mRecyclerView.setAdapter(mAdapter);

        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) root.findViewById(R.id.refresh);
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

    private void setBundle(DataBundle dataBundle) {
        CourseTable.setInstance(dataBundle.courseTable);
        bundle = new Bundle();
        bundle.putString("username", dataBundle.username);
        bundle.putString("password", dataBundle.password);
        bundle.putStringArrayList("infoTitle", dataBundle.infoTitle);
        bundle.putStringArrayList("infoTime", dataBundle.infoTime);
        bundle.putString("timeToday", dataBundle.timeToday);
        bundle.putString("timeWeek", dataBundle.timeWeek);
        bundle.putString("cardRest", dataBundle.cardRest);
        bundle.putStringArrayList("info_id", dataBundle.info_id);
        bundle.putString("course_table_str", dataBundle.course_table_str);
    }
}
