package com.example.aitongji.Home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.example.aitongji.R;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ObserverManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Novemser on 2016/2/11.
 */
public class HomePageCards extends Fragment {
    @Bind(R.id.id_home_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh)
    PullRefreshLayout pullRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, root);

        RecyclerView.Adapter mAdapter = new RecyclerAdapter(getActivity());
        ObserverManager.getInstance().setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mRecyclerView.setAdapter(mAdapter);

        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);


        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetWorkManager.getInstance().refreshAllData(mRecyclerView);
                        Snackbar.make(mRecyclerView, "正在刷新...", Snackbar.LENGTH_SHORT).show();
                        pullRefreshLayout.setRefreshing(false);

                    }
                });
            }
        });

        return root;
    }

}
