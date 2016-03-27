package com.example.aitongji.Home;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.example.aitongji.R;
import com.example.aitongji.Section_Information.Card_Rest_Notice;
import com.example.aitongji.Service.CardRestNotice;
import com.example.aitongji.WelcomeSceneAty;
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
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
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
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(origTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
    }

    public void setUpContent(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                fragmentClass = HomePageCards.class;
                break;
            case R.id.drawer_section1:
                fragmentClass = Card_Rest_Notice.class;
                break;
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("IS_AUTO", false).apply();
                editor.putBoolean("IS_CHECK", false).apply();
                editor.putBoolean("IS_FIRST", true).apply();
                CardRestNotice.cleanAllNotification();
                startActivity(new Intent(this, WelcomeSceneAty.class));
                finish();
                break;
            case R.id.about:
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
