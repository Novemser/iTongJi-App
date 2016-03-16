package com.example.aitongji.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.aitongji.R;
import com.example.aitongji.Utils.CourseTable;
import com.example.aitongji.Utils.DataBundle;
import com.example.aitongji.Utils.DataHandler;
import com.example.aitongji.Utils.GPA.GetGPA;
import com.example.aitongji.Utils.GPA.StudentGPA;
import com.example.aitongji.Utils.InformationReq;
import com.example.aitongji.WelcomeSceneAty;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Novemser on 2016/2/11.
 */
public class HomePageCards extends Fragment {
    private Bundle bundle;
    @Bind(R.id.id_home_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh)
    PullRefreshLayout pullRefreshLayout;

    private SharedPreferences sharedPreferences;
    private String TAG = "HomePageCards";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);
        setBundle((DataBundle) DataHandler.getObject(MainActivity.getContext(), "dataBundle.dat"));
        ButterKnife.bind(this, root);

        RecyclerView.Adapter mAdapter = new Home_Recycler_Adapter(bundle, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);

        if (sharedPreferences.getBoolean("IS_AUTO", false)) {
            Log.d(TAG, "Auto updating");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    pullRefreshLayout.setRefreshing(true);
                    updateData();
                }
            });
        }


            pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateData();
                }
            });

        return root;
    }

    private void updateData() {
        new InformationReq(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""), new InformationReq.SuccessCallback() {
            @Override
            public void onSuccess(final DataBundle dataBundle) {
                Log.d("Login to 4m3", "Login Succeed");
                // 保存主要信息
                DataHandler.saveObject(getActivity().getApplicationContext(), "dataBundle.dat", dataBundle);
                setBundle(dataBundle);

                // 尝试拉绩点
                new GetGPA(getActivity().getApplicationContext(), sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""), new GetGPA.SuccessCallback() {
                    @Override
                    public void onSuccess(StudentGPA studentGPA) {
                        DataHandler.saveObject(getActivity().getApplicationContext(), "studentGPA.dat", studentGPA);

                        Home_Recycler_Adapter adapter = new Home_Recycler_Adapter(bundle, getActivity());
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        mRecyclerView.invalidate();
                        pullRefreshLayout.setRefreshing(false);
                        Snackbar.make(mRecyclerView, "刷新成功", Snackbar.LENGTH_SHORT).show();
                    }
                }, new GetGPA.FailureCallback() {
                    @Override
                    public void onFailure() {
                        Snackbar.make(mRecyclerView, "多次登陆失败 请稍后再试", Snackbar.LENGTH_SHORT).show();
                        pullRefreshLayout.setRefreshing(false);
                    }
                });


            }
        }, new InformationReq.FailureCallback() {
            @Override
            public void onFailure() {
                Snackbar.make(mRecyclerView, "登陆失败 请检查网络/密码后重试", Snackbar.LENGTH_SHORT).show();
                pullRefreshLayout.setRefreshing(false);
            }
        });
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
