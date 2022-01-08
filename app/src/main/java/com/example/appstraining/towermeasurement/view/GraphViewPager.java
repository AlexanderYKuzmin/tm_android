package com.example.appstraining.towermeasurement.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.example.appstraining.towermeasurement.R;

import java.util.ArrayList;

public class GraphViewPager extends AppCompatActivity {
    final String LOG_TAG = "GraphViewPager";
    public final int XOZ = 1, YOZ = 2, XOY = 3;
    ViewPager2 viewPager2;
    public int pageCount = 3;
    public String graphTitle;
    FragmentStateAdapter fragmentStateAdapter;

    public int[] xozArray, yozArray, xoyArray;
    public int[] xozRange, yozRange, xoyRange;
    public int graphType;
    public ArrayList<int[]> graphList = new ArrayList<>();
    public ArrayList<int[]> rangeList = new ArrayList<>();
    public String[] graphTitles = {"XOZ", "YOZ", "XOY"};
    public int deviation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view_pager_activity);
        Log.d(LOG_TAG, "Content for GraphViewPager is set.");

        graphType = getIntent().getIntExtra("graphtype", 0);
        Log.d(LOG_TAG, " graphType is : " + graphType);
        xozArray = getIntent().getIntArrayExtra("xoz_array");
        yozArray = getIntent().getIntArrayExtra("yoz_array");
        xoyArray = getIntent().getIntArrayExtra("xoy_array");
        xozRange = getIntent().getIntArrayExtra("xoz_range");
        yozRange = getIntent().getIntArrayExtra("yoz_range");
        xoyRange = getIntent().getIntArrayExtra("xoy_range");
        deviation = xozArray[xozArray.length - 1]/1000;
        /*Log.d(LOG_TAG, "xozArray[0] = " + xozArray[0] + " :: xozArray[1] = " + xozArray[1] + "\n"
            + "xozArray[2] = " + xozArray[2] + " :: xozArray[3] = " + xozArray[3] + "\n"
            + "xozArray[4] = " + xozArray[4] + " :: xozArray[5] = " + xozArray[5] + "\n"
            + "xozArray[6] = " + xozArray[6] + " :: xozArray[7] = " + xozArray[7] + "\n"

        );*/
        Log.d(LOG_TAG, "YOZ array : + \n");
        for (int x : yozArray) {
            System.out.println(x + "; ");
        }
        Log.d(LOG_TAG, "XOY array : + \n");
        for (int x : xoyArray) {
            System.out.println(x + "; ");
        }
        Log.d(LOG_TAG, "XOZ Range");
        for (int x : xozRange) {
            System.out.println(x + "; ");
        }
        Log.d(LOG_TAG, "YOZ Range yozRange.length = " + yozRange.length);
        for (int x : yozRange) {
            System.out.println(x + "; ");
        }
        Log.d(LOG_TAG, "XOY Range");
        for (int x : xoyRange) {
            System.out.println(x + "; ");
        }
        /*Log.d(LOG_TAG, "XOY array : + \n"
                + xoyArray[0] + " :: " + xoyArray[1] + "\n"
                + xoyArray[2] + " :: " + xoyArray[3] + "\n"
                + xoyArray[4] + " :: " + xoyArray[5] + "\n"
                + xoyArray[6] + " :: " + xoyArray[7] + "\n"

        );*/
        graphList.add(xozArray);
        graphList.add(yozArray);
        graphList.add(xoyArray);

        rangeList.add(xozRange);
        rangeList.add(yozRange);
        rangeList.add(xoyRange);
        Log.d(LOG_TAG, "rangeList size :" + rangeList.size());

        viewPager2 = (ViewPager2) findViewById(R.id.pager);
        fragmentStateAdapter = new GraphFragmentPagerAdapter(
                this, graphTitles, graphList, pageCount, rangeList, deviation);
        viewPager2.setAdapter(fragmentStateAdapter);
    }



    public static class GraphFragmentPagerAdapter extends FragmentStateAdapter {
        private final String LOG_TAG = "GraphFragmentAdapter";
        int page_count;
        String[] titles;
        String title;
        ArrayList<int[]> graphs = new ArrayList<>(3);
        ArrayList<int[]> rangeList = new ArrayList<>(3);
        int prevPosition;
        int deviation;

        public GraphFragmentPagerAdapter(@NonNull FragmentActivity fragment, String[] titles,
                              ArrayList<int[]> graphs, int page_count, ArrayList<int[]> rangeList,
                              int deviation){
            super(fragment);
            this.titles = titles;
            this.graphs.addAll(graphs);
            this.page_count = page_count;
            this.rangeList.addAll(rangeList);
            this.deviation = deviation;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            title = titles[position];
            int[] currentGraph = graphs.get(position);
            Log.d(LOG_TAG, " currentGraph.length =" + currentGraph.length);
            /*Log.d(LOG_TAG, "currentGraph[0] = " + currentGraph[0] + "\n"
            + "currentGraph[1] = " + currentGraph[1] + "\n"
            + "currentGraph[2] = " + currentGraph[2] + "\n");*/
            return  GraphFragment.newInstance(position, title, currentGraph,
                    rangeList.get(position), deviation);
        }

        @Override
        public int getItemCount() {
            Log.d(LOG_TAG, "page_count = " + page_count);
            return page_count;
        }
    }
}