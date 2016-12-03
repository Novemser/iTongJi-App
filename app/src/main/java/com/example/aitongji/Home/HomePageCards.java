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
//    private Bundle bundle;
    @Bind(R.id.id_home_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh)
    PullRefreshLayout pullRefreshLayout;

//    private SharedPreferences sharedPreferences;
//    private String TAG = "HomePageCards";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);
//        DataBundle db = AndroidResource.dataBundle;
//        if (db != null)
//            setBundle(db);

        ButterKnife.bind(this, root);

        RecyclerView.Adapter mAdapter = new RecyclerAdapter(getActivity());
        ObserverManager.getInstance().setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mRecyclerView.setAdapter(mAdapter);

//        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);

//        int refDate = Integer.parseInt(sharedPreferences.getString("REFRESH_DATE", "-1"));
//        int refYear = Integer.parseInt(sharedPreferences.getString("REFRESH_YEAR", "-1"));
//        SimpleDateFormat sdf = new SimpleDateFormat("D");
//        final Date now = new Date();
//        final int day = Integer.parseInt(sdf.format(now));
//        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(now));

//        if (sharedPreferences.getBoolean("IS_AUTO", false) && (day - refDate) >= 1 || year != refYear) {
//            Log.d(TAG, "Auto updating");
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    pullRefreshLayout.setRefreshing(true);
////                    updateData();
//                    sharedPreferences.edit().putString("REFRESH_DATE", String.valueOf(day)).apply();
//                    sharedPreferences.edit().putString("REFRESH_YEAR", new SimpleDateFormat("yyyy").format(now)).apply();
//                }
//            }).start();
//        }


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

//    private void updateData() {
//        Log.d("Refreshing", "Start");
//        AndroidResource.setIsRefreshing(true);
//        new InformationReq(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""), new InformationReq.SuccessCallback() {
//            @Override
//            public void onSuccess(final DataBundle dataBundle) {
//                Log.d("Login to 4m3", "Login Succeed");
//                // 保存主要信息
//                if (getActivity() == null || getActivity().getApplicationContext() == null) {
//                    Log.d("Refreshing", "Stop");
//                    AndroidResource.setIsRefreshing(false);
//                    return;
//                }
//
//                AndroidResource.dataBundle = dataBundle;
//                //SerializationUtil.saveObject(getActivity().getApplicationContext(), "dataBundle.dat", dataBundle);
//                setBundle(dataBundle);
//
//                // 尝试拉绩点
//                new GetGPA(getActivity().getApplicationContext(), sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""), new GetGPA.SuccessCallback() {
//                    @Override
//                    public void onSuccess(StudentGPASubject studentGPASubject) {
//                        AndroidResource.studentGPASubject = studentGPASubject;
//                        //SerializationUtil.saveObject(getActivity().getApplicationContext(), "studentGPASubject.dat", studentGPASubject);
//
//                        Home_Recycler_Adapter adapter = new Home_Recycler_Adapter(bundle, getActivity());
//                        mRecyclerView.setAdapter(adapter);
//                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
//                        mRecyclerView.invalidate();
//                        pullRefreshLayout.setRefreshing(false);
//                        Snackbar.make(mRecyclerView, "刷新成功", Snackbar.LENGTH_SHORT).show();
//
//                        AndroidResource.setIsRefreshing(false);
//                    }
//                }, new GetGPA.FailureCallback() {
//                    @Override
//                    public void onFailure() {
//                        Snackbar.make(mRecyclerView, "多次登陆失败 请稍后再试", Snackbar.LENGTH_SHORT).show();
//                        pullRefreshLayout.setRefreshing(false);
//
//                        AndroidResource.setIsRefreshing(false);
//                    }
//                });
//
//
//            }
//        }, new InformationReq.FailureCallback() {
//            @Override
//            public void onFailure() {
//                Snackbar.make(mRecyclerView, "登陆失败 请检查网络/密码后重试", Snackbar.LENGTH_SHORT).show();
//                pullRefreshLayout.setRefreshing(false);
//
//                AndroidResource.setIsRefreshing(false);
//            }
//        });
//    }

//    private void setBundle(DataBundle dataBundle) {
////        CourseTable.setInstance(dataBundle.courseTable);
//        bundle = new Bundle();
//        bundle.putString("username", dataBundle.username);
//        bundle.putString("password", dataBundle.password);
//        bundle.putStringArrayList("infoTitle", dataBundle.infoTitle);
//        bundle.putStringArrayList("infoTime", dataBundle.infoTime);
//        bundle.putString("timeToday", dataBundle.timeToday);
//        bundle.putString("timeWeek", dataBundle.timeWeek);
//        bundle.putString("cardRest", dataBundle.cardRest);
//        bundle.putStringArrayList("info_id", dataBundle.info_id);
//        bundle.putString("course_table_str", dataBundle.course_table_str);
//    }
}
