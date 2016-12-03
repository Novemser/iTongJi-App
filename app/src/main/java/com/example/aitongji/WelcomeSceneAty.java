package com.example.aitongji;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.aitongji.Home.MainActivity;
import com.example.aitongji.Utils.Course.CourseTable;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ResourceManager;
import com.rey.material.widget.CheckBox;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class WelcomeSceneAty extends AppCompatActivity {
    @Bind(R.id.imageView)
    ImageView bg;

    private Button button;
    private EditText etAccount;
    private EditText etPW;
    private CheckBox cbRp;
    private CheckBox cbAl;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String username;
    private String password;

    public void configInputText() {
        TextInputLayout mUser = (TextInputLayout) findViewById(R.id.text_input_user_name);
        TextInputLayout mPass = (TextInputLayout) findViewById(R.id.text_input_password);
        mUser.setHint("学号");
        mPass.setHint("统一身份认证密码");
        mUser.setHintAnimationEnabled(true);
        mPass.setHintAnimationEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).load(R.drawable.login).into(bg);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_scene_aty);
        ButterKnife.bind(this);
        final MaterialProgressBar materialProgressBar = (MaterialProgressBar) findViewById(R.id.id_progressbar_welcome_circle);
        materialProgressBar.setVisibility(View.INVISIBLE);

        Glide.with(this).load(R.drawable.login).into(bg);

//        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        Log.d("TAG", "Max memory is " + maxMemory + "KB");

        final Intent intent = new Intent(this, MainActivity.class);

        configInputText();
        CourseTable.newInstance();

        initView();
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("IS_CHECK", false)) {
            // 设置记录密码状态
            cbRp.setChecked(true);
            etAccount.setText(sharedPreferences.getString("username", ""));
            etPW.setText(sharedPreferences.getString("password", ""));

            // 自动登陆处理
            if (sharedPreferences.getBoolean("IS_AUTO", false)) {
                cbAl.setChecked(true);
                username = etAccount.getText().toString();
                password = etPW.getText().toString();
                ResourceManager.getInstance().setUserName(username);
                ResourceManager.getInstance().setUserPwd(password);
                NetWorkManager.getInstance().obtainAllDataThenNotify(button.getRootView());
                startActivity(intent);
                finish();
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Snackbar.make(button.getRootView(), "网络连接似乎不太好 X﹏X", Snackbar.LENGTH_SHORT).setAction("知道啦", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    return;
                }
                button.setEnabled(false);
                Log.d("BTN", "clicked");
                button.setText("正在登录...");
                materialProgressBar.setVisibility(View.VISIBLE);
                username = etAccount.getText().toString();
                password = etPW.getText().toString();
                ResourceManager.getInstance().setUserName(username);
                ResourceManager.getInstance().setUserPwd(password);
                NetWorkManager.getInstance().obtainAllDataThenNotify(button.getRootView());
                editor.putString("username", username);
                editor.putString("password", password);
                editor.apply();
                startActivity(intent);
                finish();
            }
        });
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void initView() {
        etAccount = (EditText) findViewById(R.id.id_welcome_et_username);
        etPW = (EditText) findViewById(R.id.id_welcome_et_passwprd);
        cbRp = (CheckBox) findViewById(R.id.id_checkbox_rp);
        cbAl = (CheckBox) findViewById(R.id.id_checkbox_al);
        button = (Button) findViewById(R.id.welcome_scene_btn);

        cbRp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbRp.isChecked()) {
                    sharedPreferences.edit().putBoolean("IS_CHECK", true).apply();
                } else {
                    sharedPreferences.edit().putBoolean("IS_CHECK", false).apply();
                }
            }
        });

        cbAl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbAl.isChecked() && cbRp.isChecked()) {
                    sharedPreferences.edit().putBoolean("IS_AUTO", true).apply();
                } else {
                    sharedPreferences.edit().putBoolean("IS_AUTO", false).apply();
                }
            }
        });
    }


}
