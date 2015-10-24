package com.example.aitongji;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeSceneAty extends AppCompatActivity {


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

        final Intent intent = new Intent(this, MainActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        configInputText();


        // 2.5s后进入主界面
        timer.schedule(task, 250000);
    }
}
