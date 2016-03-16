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

public class Card_Information extends ToolbarActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected int provideContentViewId() {
        return R.layout.card_information_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
//        setContentView(R.layout.card_information_layout);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mToolbar.setTitle("iTongJi");
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            mAppBar.setElevation(10.6f);
//        }

        mRecyclerView = (RecyclerView) findViewById(R.id.id_card_info_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new Card_Info_Adapter(intent.getStringArrayListExtra("info_title"), intent.getStringArrayListExtra("info_time"), intent.getStringExtra("username"), intent.getStringExtra("password"), intent.getStringArrayListExtra("info_id")));
    }

}
