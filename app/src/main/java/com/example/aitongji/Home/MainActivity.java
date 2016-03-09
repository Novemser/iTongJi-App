package com.example.aitongji.Home;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.example.aitongji.R;
import com.example.aitongji.Section_Information.Card_Rest_Notice;
import com.example.aitongji.Section_Information.Information;
import com.example.aitongji.Service.CardRestNotice;
import com.example.aitongji.Utils.GetGPA;
import com.example.aitongji.WelcomeSceneAty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.novemser.ocrtest.BP;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private final static String CHECK_IMAGE = "http://xuanke.tongji.edu.cn/CheckImage";
    private final static String URL_LOGIN = "http://tjis2.tongji.edu.cn:58080/amserver/UI/Login";
    private final static String LOGIN_TREE = "http://xuanke.tongji.edu.cn/tj_login/loginTree.jsp";
    private String GPA_QUERY = "http://xuanke.tongji.edu.cn/tj_login/redirect.jsp?link=/tj_xuankexjgl/score/query/student/cjcx.jsp?";
    private String qxid;
    private String mkid;
    private ArrayList<Bitmap> pieces;
    private String result;
    private BP bp;
    private Bitmap bitmap;

    private int getMax(double[] input) {
        double tmp = -1;
        int j = 0;
        for (int i = 0; i < 10; i++) {
            if (input[i] > tmp) {
                tmp = input[i];
                j = i;
            }
        }
        return j;
    }

    private void spiltCheckImage(Bitmap bitmap) {
        pieces.clear();
        for (int i = 0; i < 4; i++) {
            pieces.add(Bitmap.createBitmap(bitmap, 2 + i * 9, 3, 8, 12));
        }
    }

    public void tes() {
        Button button = (Button) findViewById(R.id.btn);
        imageView = (ImageView) findViewById(R.id.imgTest);
        pieces = new ArrayList<>();

        // 从Assets文件夹读取BP神经网络
        final AssetManager assetManager = MainActivity.getContext().getAssets();
        try {
            ObjectInputStream ois = new ObjectInputStream(assetManager.open("bp.dat"));
            bp = (BP) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                PersistentCookieStore persistentCookieStore = new PersistentCookieStore(MainActivity.this);
                persistentCookieStore.clear();
                asyncHttpClient.setCookieStore(persistentCookieStore);
                asyncHttpClient.get(CHECK_IMAGE, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // 获取验证码
                        System.out.println(statusCode + " " + statusCode);
                        bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                        handler.sendEmptyMessage(0); // 设置图片

                        if (bitmap != null) {
                            spiltCheckImage(bitmap);
                        }

                        result = "";
                        for (Bitmap item : pieces) {
                            double[] input = new double[96];
                            int cnt = 0;
                            for (int i = item.getHeight() - 1; i >= 0; i--)
                                for (int j = item.getWidth() - 1; j >= 0; j--) {
                                    int R = Color.red(item.getPixel(j, i));
                                    if (R > 200) {
                                        input[cnt++] = 1.0;
                                    } else {
                                        input[cnt++] = 0.0;
                                    }
                                }
                            double[] res = bp.test(input);
                            result += getMax(res);
                        }

                        // Login
                        final RequestParams params = new RequestParams();
                        params.put("Login.Token1", "1452681");
                        params.put("Login.Token2", "a20131002");
                        params.put("T3", result);
                        params.put("goto", "http://xuanke.tongji.edu.cn/pass.jsp?checkCode=" + result);
                        params.put("gotoOnFail", "http://xuanke.tongji.edu.cn/deny.jsp?checkCode=" + result + "&account=1452681&password=BED0648254DF3D9BA9350683817275CE");


                        asyncHttpClient.post(URL_LOGIN, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                asyncHttpClient.get("http://xuanke.tongji.edu.cn/pass.jsp?checkCode=" + result, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                        for (Header header : headers) {
                                            System.out.println(header.getName() + ":" + header.getValue());
                                        }

                                        // length 小于2000才是成功 否则密码错误
                                        int len = Jsoup.parse(new String(responseBody)).body().html().length();
                                        if (len < 2000) {
                                            Log.d("登陆:", "Success!");
//                                            System.out.println(Jsoup.parse(new String(responseBody)).body().html());

                                            // 获取权限id
                                            asyncHttpClient.get(LOGIN_TREE, new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                    System.out.println("Success for getting id:" + statusCode);
                                                    try {
                                                        String auth = new String(responseBody, "gb2312");
                                                        String jsonString = "";
                                                        Pattern pattern = Pattern.compile("\\{  ICO:\"null\", ID:\"\\d+\", DKFS:\"\\d+\", URL:\"/tj_xuankexjgl/score/query/student/cjcx.jsp\", MKPXH:\"\\d\", MKBH:\"((.*?))\", MKMC:\"成绩查询\", MKLX:\"\\d+\", SJMK_ID:\"\\d+\", QX_ID:\"\\d+\", QXMC:\"全部权限\", HELPURL:\"null\", MYXSJL:\"null\" \\}");
                                                        Matcher matcher = pattern.matcher(auth);
                                                        while (matcher.find()) {
//                                                            System.out.println(matcher.group(0));
                                                            jsonString = matcher.group(0);
                                                        }
                                                        pattern = Pattern.compile("ID:\"(.*?)\"");
                                                        matcher = pattern.matcher(jsonString);
                                                        int cnt = 0;
                                                        while (matcher.find()) {
                                                            if (cnt == 0) {
                                                                mkid = matcher.group(1);
                                                                System.out.println("Mkid:" + mkid);
                                                                cnt++;
                                                            } else if (cnt == 2) {
                                                                qxid = matcher.group(1);
                                                                System.out.println("Qxid:" + qxid);
                                                                break;
                                                            } else {
                                                                cnt++;
                                                            }
                                                        }


                                                        // 拉出绩点表
                                                        GPA_QUERY += "qxid=" + qxid + "$mkid=" + mkid + "&qxid=" + qxid + "&HELP_URL=null&MYXSJL=null";
                                                        asyncHttpClient.get(GPA_QUERY, new AsyncHttpResponseHandler() {
                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                                System.out.println("GPA success:" + statusCode);
                                                                try {
                                                                    System.out.println(Jsoup.parse(new String(responseBody, "gb2312")).body().text());
                                                                } catch (UnsupportedEncodingException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                            @Override
                                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                            }
                                                        });
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                }
                                            });

                                        } else {
                                            System.out.println("验证码识别错误!");
                                        }

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    }
                                });

                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("Handle message called.");
            imageView.setImageBitmap(bitmap);
        }
    };

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private ImageView imageView;

    private CharSequence origTitle;
    private FragmentManager fm;
    private Class fragmentClass;
    private static Context sContext = null;

    ArrayList<Class> isCreated;
    ArrayList<Fragment> fragments;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        tes();
    }

    public void setUpContent(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        System.out.println(menuItem.getItemId());
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
                finish();
                startActivity(new Intent(this, WelcomeSceneAty.class));
                break;
            case R.id.setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                fragmentClass = Information.class;
        }

        transFragment();
    }

    private void transFragment() {
        try {
            if (!isCreated.contains(fragmentClass)) {
                isCreated.add(fragmentClass);
                fragment = (Fragment) fragmentClass.newInstance();
                if (fragmentClass == HomePageCards.class || fragmentClass == Card_Rest_Notice.class) {
                    fragment.setArguments(getIntent().getExtras());
                }
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

    public static Context getContext() {
        return sContext;
    }
}
