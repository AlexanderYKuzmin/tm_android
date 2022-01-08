package com.example.appstraining.towermeasurement.model;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TowerGLRenderer implements GLSurfaceView.Renderer {
    private final String LOG_TAG = "TowerGLRenderer";
    private TowerModel mTowerModel;
    float[] flats;
    float[] edges;
    int config;
    int levels;

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float[] rotationMatrix = new float[16];

    public TowerGLRenderer(float[] flats, float[] edges, int config, int levels) {
        this.flats = flats;
        this.edges = edges;
        this.config = config;
        this.levels = levels;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // initialize a triangle
        mTowerModel = new TowerModel(flats, edges, this.config, levels);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
       // width = 600;
        //height = 800;
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width/height;
        Log.d(LOG_TAG, "onSurfaceChanged. ratio = " + ratio);
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 2, 5f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];
        Log.d(LOG_TAG, "On draw frame");
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix,0, 0, 2f, -3, 0f, 0f,
                0.0f, 0.0f, 1.0f, 1.0f );

        // Create a rotation transformation for the triangle


        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        //mTriangle.draw(vPMatrix);

        // Create a rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % 25000L;
        float angle = 0.05f * ((int) time);
        Matrix.setRotateM(rotationMatrix, 0, angle, 0.0f, 1f, 0.0f);

        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);
        mTowerModel.draw(scratch);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
