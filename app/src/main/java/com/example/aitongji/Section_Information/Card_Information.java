package com.example.aitongji.Section_Information;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aitongji.activity.ToolbarActivity;
import com.example.aitongji.R;
import com.umeng.analytics.MobclickAgent;

public class Card_Information extends ToolbarActivity {
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.card_information_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("信息通知");
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_card_info_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new Card_Info_Adapter());
    }

}
