package com.example.appstraining.towermeasurement.model;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TowerModel {
    private final String LOG_TAG = " TowerModel";
    private FloatBuffer vertexBuffer;
    private final int mProgram;

    private int positionHandle;
    private int colorHandle;
    // Use to access and set the view transformation
    private int vPMatrixHandle;


    //private final int vertexCount = superArray.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

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

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float flats[];
    static float edges[];
    static int config;
    static int levels;
    float[] superArray;

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {
          0.63671875f, 0.76953125f, 0.22265625f, 1.0f
    };
    /*float colorBlue[] = {
            0.2f, 0.4f, 0.8f, 0.2f,
    };*/

    public TowerModel(float[] flats, float[] edges, int config, int levels) {
        this.flats = flats;
        this.edges = edges;
        this.config = config;
        this.levels = levels;
        superArray = joinArray(flats, edges);
        //superArray = edges;
        Log.d(LOG_TAG, " Creating the Tower. Length of SuperArray = " + superArray.length);
        //this.vertices = vertices;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                superArray.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());


        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(superArray);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        /*
        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());

        ShortBuffer drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
*/

        int vertexShader = TowerGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int vertexFragment = TowerGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);
        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, vertexFragment);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {// pass in the calculated transformation matrix

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false, vertexStride, vertexBuffer);
        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the tower
        //GLES20.glUniform4f(colorHandle, 0.2f, 0.2f, 0.5f, 0.0f);
        int shift;
        int position = 0;
        for(int i = 0; i < levels; i++) {
            shift = i * (config + 1);
            for (int j = 0; j < config; j++) {
                //GLES20.glDrawArrays(GLES20.GL_LINES, j + shift, 2);
                position = j + shift;
                GLES20.glDrawArrays(GLES20.GL_LINES, position, 2);
                Log.d(LOG_TAG, "Trying to draw flats..  "
                        + " x = " + superArray[position * 3]
                        + " y = " + superArray[position * 3 + 1]
                        + " z = " + superArray[position * 3 + 2]

                );
                //GLES20.glDrawArrays(GLES20.GL_LINES, j + 1 + shift, 2);
                //GLES20.glDrawArrays(GLES20.GL_LINES, j + shift, 2);
            }
        }
        position += 2;
        //int position = flats.length;
        //int position = 0;
        for (int i = 0; i < config; i++) {
            for(int j = 0; j < levels - 1; j++) {
                Log.d(LOG_TAG, "Trying to draw edges.. " + superArray[position*3] + "; "
                        + superArray[position*3 + 1] + "; "
                        + superArray[position*3 + 2]);
                GLES20.glDrawArrays(GLES20.GL_LINES, position, 2);
                position ++;
            }
            position ++;
        }
        //GLES20.glDrawArrays(GLES20.GL_LINES, flats.length, 2);

       /* GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);

        //GLES20.glUniform4fv(colorHandle, 2, colorBlue, 0);
        //GLES20.glUniform4f(colorHandle, 1.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 2, 2);

        //GLES20.glUniform4f(colorHandle, 0.0f, 1.0f, 0.0f, 0.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 4, 2);

        //GLES20.glUniform4f(colorHandle, 0.0f, 0.0f, 1.0f, 0.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        GLES20.glDrawArrays(GLES20.GL_LINES, 8, 2);

        //GLES20.glUniform4f(colorHandle, 0.0f, 0.0f, 1.0f, 0.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 10, 2);
*/
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);

    }

    private float[] joinArray(float[] flats, float[] edges) {
        float[] superArray = new float[flats.length + edges.length];
        for(int i = 0; i < superArray.length; i++) {
            if(i < flats.length) {
                superArray[i] = flats[i];
                Log.d(LOG_TAG, "SuperArray [" + i + "] = " + superArray[i] + " from flats");
            } else {
                superArray[i] = edges[i - flats.length];
                Log.d(LOG_TAG, "SuperArray [" + i + "] = " + superArray[i] + " from edges");
            }
        }
        return superArray;
    }
}
