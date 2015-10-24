package com.example.aitongji;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aitongji.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;

    private CharSequence origTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nv_view);
//        mListView = (ListView) findViewById(R.id.left_drawer);
//        menuList = new ArrayList<String>();

//        menuList.add("信息推送");
//        menuList.add("课表查询");
//        menuList.add("一卡通信息");
//        menuList.add("二手交易");
//        menuList.add("绩点查询");
//        menuList.add("评教系统");
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
//        mListView.setAdapter(adapter);

//        mListView.setOnItemClickListener(new DrawerLayoutItemListener());


        // Left Menu
//        ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < 6; i++) {
//            Map<String, Object> item = new HashMap<String, Object>();
//            item.put("image", mImage[i]);
//            item.put("text", mTitle[i]);
//            mData.add(item);
//        }
//        Map<String, Object> item = new HashMap<String, Object>();
//        item.put("image", R.drawable.phone);
//        item.put("text", "我也是醉了");
//        mData.add(item);
//        item = new HashMap<String, Object>();
//        item.put("image", R.drawable.note);
//        item.put("text", R.string.course_timetable);
//        mData.add(item);

        // Simple Adapter
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, mData, R.layout.left_menu_unit,
//                new String[]{"image", "text"}, new int[]{R.id.image_view, R.id.text_view});
//        mListView.setAdapter(simpleAdapter);

        // Listener
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "hhh", Toast.LENGTH_SHORT).show();
//                switch (position) {
//                    case 0:
//                        Fragment cF = new Section1_infomation();
//                        getFragmentManager().beginTransaction()
//                                .replace(R.id.content_frame, cF)
//                                .commit();
//                        break;
//                    case 1:
//
//                        break;
//                    case 2:
//
//                        break;
//                    case 3:
//
//                        break;
//                    case 4:
//
//                        break;
//                    case 5:
//
//                        break;
//                }
//                mDrawerLayout.closeDrawer(mListView);
//                System.out.println(position);
//            }
//        });

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
//                getSupportActionBar().setTitle("Plz select");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(origTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        // 2015.10.24
        // Learn Volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://www.baidu.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(getApplication()).add(stringRequest);
        // Learn Volley
    }

    public void setUpContent(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        System.out.println(menuItem.getItemId());
                        Snackbar.make(findViewById(R.id.myToolBar), menuItem.getTitle() + " pressed", Snackbar.LENGTH_SHORT).show();
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(position);
    }


//    private class DrawerLayoutItemListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //
//            System.out.println(position);
//            }
//            Fragment contentFragment = new ContentFragment();
//            Bundle args = new Bundle();
//            args.putString("text", menuList.get(position) + " Fucker");
//            contentFragment.setArguments(args);
//
//            FragmentManager fm = getFragmentManager();
//            fm.beginTransaction()
//                    .replace(R.id.content_frame, contentFragment)
//                    .commit();
//
//            mDrawerLayout.closeDrawer(mListView);
//        }
//    }
}
