package com.example.aitongji.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.aitongji.R;

/**
 * Created by Novemser on 2016/2/15.
 */
public abstract class ToolbarActivity extends BaseActivity {
    abstract protected int provideContentViewId();

    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;
    protected boolean mIsHidden = false;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());

        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("iTongji");

        if (mToolbar == null || mAppBar == null) {
            throw new IllegalStateException("No toolbar");
        }

        setSupportActionBar(mToolbar);

        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            mAppBar.setElevation(10.6f);
        }
    }

    public boolean canBack() {
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

}
