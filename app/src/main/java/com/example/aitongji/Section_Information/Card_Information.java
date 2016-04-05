package com.example.aitongji.Section_Information;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Window;

import com.example.aitongji.Base.ToolbarActivity;
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
        Intent intent = getIntent();
        mToolbar.setTitle("信息通知");
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_card_info_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new Card_Info_Adapter(intent.getStringArrayListExtra("info_title"), intent.getStringArrayListExtra("info_time"), intent.getStringExtra("username"), intent.getStringExtra("password"), intent.getStringArrayListExtra("info_id")));
    }

}
