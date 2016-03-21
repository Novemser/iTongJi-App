package com.example.aitongji.Section_Elect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aitongji.Base.ToolbarActivity;
import com.example.aitongji.R;

public class ElectricityQuery extends ToolbarActivity {

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_electricity_query;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle("电费查询");
    }
}
