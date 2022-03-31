package com.example.appstraining.towermeasurement.view.main.fragments;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.openGL.TowerGLRenderer;
import com.example.appstraining.towermeasurement.view.main.fragments.TowerModelingFragment;

public class TowerModelingFragment extends Fragment {
    private final String LOG_TAG = "TowerFragment";
    private GLSurfaceView gLView;
    float[] towerFlats;
    float[] towerEdges;
    int config;
    int levels;

    public static TowerModelingFragment newInstance() {

        return new TowerModelingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.

        towerFlats = getArguments().getFloatArray("towerflats");
        towerEdges = getArguments().getFloatArray("toweredges");
        config = getArguments().getInt("config");
        levels = getArguments().getInt("levels");
        Log.d(LOG_TAG, "Creating glView");

        gLView = new TowerGLSurfaceView(getContext());
        //gLView = findViewById(R.id.sv);
        View rootView = inflater.inflate(R.layout.fragment_tower_modeling, container, false);

        return gLView;
    }

    class TowerGLSurfaceView extends GLSurfaceView {

        private final TowerGLRenderer renderer;

        public TowerGLSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);
            Log.d(LOG_TAG, " Creating renderer");
            renderer = new TowerGLRenderer(towerFlats, towerEdges, config, levels);

            // Set the Renderer for drawing on the GLSurfaceView
            Log.d(LOG_TAG, " Set renderer to TowerGLSurfaceView");
            setRenderer(renderer);
            // Render the view only when there is a change in the drawing data
            // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        }
    }
}
