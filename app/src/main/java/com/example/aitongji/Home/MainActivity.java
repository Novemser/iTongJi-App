package com.example.aitongji.Home;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aitongji.About;
import com.example.aitongji.R;
import com.example.aitongji.Section_Information.Card_Rest_Notice;
import com.example.aitongji.Service.CardRestNotice;
import com.example.aitongji.Utils.AndroidResource;
import com.example.aitongji.WelcomeSceneAty;
import com.qihoo.appstore.updatelib.UpdateManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;

    private CharSequence origTitle;
    private FragmentManager fm;
    private Class fragmentClass;
    private static Context sContext = null;
    private long mExitTime;

    ArrayList<Class> isCreated;
    ArrayList<Fragment> fragments;
    Fragment fragment;
    private String TAG = "MAIN ACTIVITY";
    int drawerState;
    private MenuItem tmpMenuItem;

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
    protected void onCreate(Bundle savedInstanceState) {
        // 设置透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            w.setEnterTransition(new Explode());
//            w.setExitTransition(new Explode());

        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sContext = this;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(sContext);

        if (preferences.getBoolean("service_state", false)) {
            String value = preferences.getString("card_rest_value_text", "10000");
            Intent serviceIntent = new Intent(MainActivity.getContext(), CardRestNotice.class);
            getSharedPreferences("user", MODE_PRIVATE).edit().putString("value", value).apply();
            MainActivity.getContext().startService(serviceIntent);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nv_view);

        isCreated = new ArrayList<>();
        fragments = new ArrayList<>();
        fragment = null;

        fragmentClass = HomePageCards.class;
        transFragment();

        // ToolBar Configuration
        // Cannot use this with action bar, so
        // mind the styles.xml
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolBar);
        toolbar.setTitle("iTongJi");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set up Drawer content
        setUpContent(mNavigationView);

        // ActionBarDrawerConfig
        origTitle = getSupportActionBar().getTitle();
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerState = 1;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(origTitle);
                drawerState = 0;
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        Log.d("UD", "UD start");
        UpdateManager.setDebug(true);
        UpdateManager.checkUpdate(this);
        Log.d("UD", "UD end");
    }

    public void setUpContent(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // 正在刷新，禁止点击
                        if (AndroidResource.isRefreshing()) {
                            Snackbar.make(mDrawerLayout, "正在刷新数据，请稍后", Snackbar.LENGTH_SHORT).show();
                            return false;
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }

    public void selectDrawerItem(final MenuItem menuItem) {
        getSupportActionBar().setElevation(4);
        tmpMenuItem = menuItem;
        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                fragmentClass = HomePageCards.class;
                break;
            case R.id.drawer_section1:
                fragmentClass = Card_Rest_Notice.class;
                break;
            case R.id.logout:
                new MaterialDialog.Builder(this)
                        .title("提示")
                        .content("确定要注销吗？~")
                        .negativeText("呀,点错了")
                        .positiveText("确定~")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("IS_AUTO", false).apply();
                                editor.putBoolean("IS_CHECK", false).apply();
                                editor.putBoolean("IS_FIRST", true).apply();
                                CardRestNotice.cleanAllNotification();
                                startActivity(new Intent(MainActivity.this, WelcomeSceneAty.class));
                                finish();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                menuItem.setChecked(false);
                            }
                        })
                        .show();
                break;
            case R.id.about:
                fragmentClass = About.class;
                getSupportActionBar().setElevation(0);
                break;
            default:
                fragmentClass = HomePageCards.class;
        }

        transFragment();
    }

    private void transFragment() {
        try {
            if (!isCreated.contains(fragmentClass)) {
                isCreated.add(fragmentClass);
                fragment = (Fragment) fragmentClass.newInstance();
                fragments.add(fragment);
            } else {
                fragment = fragments.get(isCreated.indexOf(fragmentClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.id_framelayout, fragment).commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerState == 1) {
                mDrawerLayout.closeDrawers();
                drawerState = 0;
                return true;
            }

            if (!fragmentClass.equals(HomePageCards.class)) {
                fragmentClass = HomePageCards.class;
                transFragment();
                if (tmpMenuItem != null && tmpMenuItem.getItemId() != R.id.drawer_home) {
                    tmpMenuItem.setChecked(false);
                }
                return true;
            }

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Snackbar.make(mNavigationView, "再按一次退出", Snackbar.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
