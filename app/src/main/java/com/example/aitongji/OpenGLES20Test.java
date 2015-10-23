package com.example.aitongji;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Novemser on 2015/10/23.
 */
public class OpenGLES20Test extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new MyGLSurfaceView(this);
        setContentView(glSurfaceView);
    }
}

