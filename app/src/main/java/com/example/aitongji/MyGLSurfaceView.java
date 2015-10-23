package com.example.aitongji;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Novemser on 2015/10/23.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer myGLRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an GL ES 2.0 content
        setEGLContextClientVersion(2);

        myGLRenderer = new MyGLRenderer();

        setRenderer(myGLRenderer);

        // Render the view only when there is a change in the drawing data
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
