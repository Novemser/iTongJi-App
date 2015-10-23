package com.example.aitongji.Graphics;

import android.opengl.GLES20;

import com.example.aitongji.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Novemser on 2015/10/23.
 */
public class Triangle {

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    private FloatBuffer vertexBuffer;

    private final int mProgram;

    static final int COORDS_PRE_VERTEX = 3;
    static float triangleCoords[] = {
            0.0f, 0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    float color[] = {
            0.63671875f, 0.76953125f, 0.22265625f, 1.0f
    };

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PRE_VERTEX;
    private final int vertexStride = COORDS_PRE_VERTEX * 4;

    public Triangle() {
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);


        // Initialize the shader
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // Create empty OpenGL ES program
        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        GLES20.glLinkProgram(mProgram);
    }

//    public void draw() {
//        GLES20.glUseProgram(mProgram);
//
//        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PRE_VERTEX,
//                GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
//        //
//        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
//        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
//        GLES20.glDisableVertexAttribArray(mPositionHandle);
//    }

    public void draw(float[] mvpMatrix) { // pass in the calculated transformation matrix
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
