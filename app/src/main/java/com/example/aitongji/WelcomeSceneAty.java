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

import com.example.aitongji.Home.MainActivity;
import com.example.aitongji.Utils.CourseTable;
import com.example.aitongji.Utils.InformationReq;
import com.rey.material.widget.CheckBox;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class WelcomeSceneAty extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_welcome_scene_aty);
        final MaterialProgressBar materialProgressBar = (MaterialProgressBar) findViewById(R.id.id_progressbar_welcome_circle);
        materialProgressBar.setVisibility(View.INVISIBLE);

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d("TAG", "Max memory is " + maxMemory + "KB");

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

                if (!isNetworkAvailable()) {
                    Snackbar.make(button.getRootView(), "网络连接似乎不太好 X﹏X", Snackbar.LENGTH_SHORT).setAction("知道啦", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                } else {
                    button.setText("正在登录...");
                    materialProgressBar.setVisibility(View.VISIBLE);
                    new InformationReq(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""), new InformationReq.SuccessCallback() {
                        @Override
                        public void onSuccess(Bundle data) {
                            System.out.println("Succeed");
                            materialProgressBar.setVisibility(View.INVISIBLE);
                            button.setText("登录成功");

                            intent.putExtras(data);
                            startActivity(intent);

                            finish();
                        }
                    }, new InformationReq.FailureCallback() {
                        @Override
                        public void onFailure() {
                            materialProgressBar.setVisibility(View.INVISIBLE);
                            button.setText("立即登录");
                            Snackbar.make(button.getRootView(), "用户名或密码不正确", Snackbar.LENGTH_SHORT).setAction("知道了", null).show();
                        }
                    });
                }
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
                } else {
                    button.setText("正在登录...");
                    materialProgressBar.setVisibility(View.VISIBLE);
                    username = etAccount.getText().toString();
                    password = etPW.getText().toString();

                    new InformationReq(username, password, new InformationReq.SuccessCallback() {
                        @Override
                        public void onSuccess(Bundle data) {
                            System.out.println("Login Succeed");
                            materialProgressBar.setVisibility(View.INVISIBLE);
                            button.setText("登录成功");

                            // 记住用户名和密码
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.apply();

                            intent.putExtras(data);
                            startActivity(intent);

                            finish();
                        }
                    }, new InformationReq.FailureCallback() {
                        @Override
                        public void onFailure() {
                            materialProgressBar.setVisibility(View.INVISIBLE);
                            button.setText("立即登录");
                            Snackbar.make(button.getRootView(), "用户名或密码不正确", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }

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
                    System.out.println("记住密码选中");
                    sharedPreferences.edit().putBoolean("IS_CHECK", true).apply();
                } else {
                    System.out.println("记住密码未选中");
                    sharedPreferences.edit().putBoolean("IS_CHECK", false).apply();
                }
            }
        });

        cbAl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbAl.isChecked()) {
                    System.out.println("自动登录选中");
                    sharedPreferences.edit().putBoolean("IS_AUTO", true).apply();
                } else {
                    System.out.println("自动登录未选中");
                    sharedPreferences.edit().putBoolean("IS_AUTO", false).apply();
                }
            }
        });
    }


}
