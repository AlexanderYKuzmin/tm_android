package com.example.appstraining.towermeasurement.view.result;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appstraining.towermeasurement.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

public class GraphFragment extends Fragment {
    private final String LOG_TAG = "GraphFragment";
    static final String ARGUMENT_GRAPH_TITLE = "arg_page_title";
    static final String ARGUMENT_GRAPH_NUMBER = "arg_page_number";
    static final String ARGUMENT_HEIGHT = "height";
    static final String ARGUMENT_ARRAY = "points";
    static final String ARGUMENT_RANGE = "range";
    static final String ARGUMENT_DEVIATION = "deviation";

    public String graphTitle;
    public int[] graphPointsData;
    public int[] graphRange;
    public int position;
    public int height;
    public int deviation;

    public ArrayList<LineGraphSeries> lineSeriesList = new ArrayList<>();
    public ArrayList<PointsGraphSeries> pointSeriesList = new ArrayList<>();

    static GraphFragment newInstance(int position, String title, int[] currentGraph,
                                     int[] currentGraphRange, int deviation) {
        GraphFragment graphFragment = new GraphFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_GRAPH_NUMBER, position);
        arguments.putString(ARGUMENT_GRAPH_TITLE, title);
        arguments.putIntArray(ARGUMENT_ARRAY, currentGraph);
        arguments.putInt(ARGUMENT_HEIGHT, currentGraph[currentGraph.length - 1]);
        arguments.putInt(ARGUMENT_DEVIATION, deviation);
        arguments.putIntArray(ARGUMENT_RANGE, currentGraphRange);
        graphFragment.setArguments(arguments);
        return graphFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        graphTitle = getArguments().getString(ARGUMENT_GRAPH_TITLE);
        graphPointsData = getArguments().getIntArray(ARGUMENT_ARRAY);
        /*Log.d(LOG_TAG, "graphPointData[5] = " + graphPointsData[5] + "\n"
                + "graphPointData[6] = " + graphPointsData[6] + "\n"
                + "graphPointData[7] = " + graphPointsData[7] + "\n"
                + "graphPointData[8] = " + graphPointsData[8] + "\n"
                + "graphPointData[9] = " + graphPointsData[9] + "\n"
                + "graphPointData[10] = " + graphPointsData[10] + "\n"
                + "graphPointData[18] = " + graphPointsData[18] + "\n"
                + "graphPointData[19] = " + graphPointsData[19] + "\n"
        );*/
        position = getArguments().getInt(ARGUMENT_GRAPH_NUMBER);
        height = getArguments().getInt(ARGUMENT_HEIGHT);
        graphRange = getArguments().getIntArray(ARGUMENT_RANGE);
        deviation = getArguments().getInt(ARGUMENT_DEVIATION);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.graph_page_fragment, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tvGraphTitle);
        tvTitle.setText(graphTitle);

        Log.d(LOG_TAG, " in CreateView graphPointsData.length = " + graphPointsData.length);
        //DataPoint[] lineDataPointArray = new DataPoint[graphPointsData.length/2];

        DataPoint[] lineDataPoint = new DataPoint[2];
        DataPoint[] pDataPoint = new DataPoint[1];

        for (int i = 0; i < graphPointsData.length - 2; i += 2) {
            pDataPoint[0] = new DataPoint(graphPointsData[i + 2], graphPointsData[i + 3]);
            if(graphPointsData[i] > graphPointsData[i + 2]) {
                lineDataPoint[0] = new DataPoint(graphPointsData[i + 2], graphPointsData[i + 3]);
                lineDataPoint[1] = new DataPoint(graphPointsData[i], graphPointsData[i+1]);
            } else {
                if (graphPointsData[i] == graphPointsData[i + 2]) graphPointsData[i + 2] += 0.01;
                lineDataPoint[0] = new DataPoint(graphPointsData[i], graphPointsData[i+1]);
                lineDataPoint[1] = new DataPoint(graphPointsData[i + 2], graphPointsData[i + 3]);
            }
            lineSeriesList.add(new LineGraphSeries(lineDataPoint));

            PointsGraphSeries pointsGraphSeries = new PointsGraphSeries(pDataPoint);
            pointsGraphSeries.setSize(12);
            if(!graphTitle.equals("XOY")) {
                if(pDataPoint[0].getX() > pDataPoint[0].getY()/1000) {
                    pointsGraphSeries.setColor(Color.RED);
                }
            } else {
                if(Math.abs(pDataPoint[0].getX()) > deviation || Math.abs(pDataPoint[0].getY()) > deviation) {
                    pointsGraphSeries.setColor(Color.RED);
                }
            }
            Log.d(LOG_TAG, "i = " + i + "; pDataPoint[0] = " + pDataPoint[0].getX() + " : " + pDataPoint[0].getY());
            pointSeriesList.add(pointsGraphSeries);
        }

        GraphView graphView = (GraphView) view.findViewById(R.id.graph);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        Log.d(LOG_TAG, "Deviation = " + deviation);

        if(graphTitle.equals("XOZ") || graphTitle.equals("YOZ")) {
            graphView.getViewport().setMinX(graphRange[ReportPreparePresenter.MIN_X] - 5);
            graphView.getViewport().setMaxX(graphRange[ReportPreparePresenter.MAX_X] + 5);
            graphView.getViewport().setMinY(graphRange[ReportPreparePresenter.MIN_Y]);
            graphView.getViewport().setMaxY(graphRange[ReportPreparePresenter.MAX_Y]);

            //graphView.getGridLabelRenderer().setNumHorizontalLabels(
              //      graphRange[ReportPreparePresenter.MAX_X] / 5);
            graphView.getGridLabelRenderer().setNumVerticalLabels(
                    graphRange[ReportPreparePresenter.MAX_Y]/1000);

            graphView.addSeries(getBorderSeries());

        } else {   // For XOY graphic

            if(graphRange[ReportPreparePresenter.MIN_X]  < -deviation ||
                graphRange[ReportPreparePresenter.MAX_X] > deviation ||
                graphRange[ReportPreparePresenter.MIN_Y] < -deviation ||
                graphRange[ReportPreparePresenter.MAX_Y] > deviation
            ) {
                graphView.getViewport().setMinX(graphRange[ReportPreparePresenter.MIN_X]);
                graphView.getViewport().setMaxX(graphRange[ReportPreparePresenter.MAX_X]);
                graphView.getViewport().setMinY(graphRange[ReportPreparePresenter.MIN_Y]);
                graphView.getViewport().setMaxY(graphRange[ReportPreparePresenter.MAX_Y]);
            } else {
                graphView.getViewport().setMinX(-deviation - 5);
                graphView.getViewport().setMaxX(deviation + 5);
                graphView.getViewport().setMinY(-deviation - 5);
                graphView.getViewport().setMaxY(deviation + 5);
            }
            graphView.getGridLabelRenderer().setNumVerticalLabels(
                    graphRange[ReportPreparePresenter.MAX_Y]/5);
            graphView.getGridLabelRenderer().setNumVerticalLabels(
                    graphRange[ReportPreparePresenter.MAX_Y]/5);


            graphView.addSeries(getRoundSeries(deviation));
        }

        for (LineGraphSeries s : lineSeriesList) {
            s.setColor(Color.BLUE);
            graphView.addSeries(s);
        }

        for(PointsGraphSeries s : pointSeriesList) {
            graphView.addSeries(s);
        }
        /*for(int i = 0, j = 0; i < graphPointsData.length; i += 2, j++){
            //Log.d(LOG_TAG, " i = " + i + "; graphPointsData[i] = " + graphPointsData[i]);
            //Log.d(LOG_TAG, " j = " + j);
            lineDataPointArray[j] = new DataPoint((double)graphPointsData[i], (double)graphPointsData[i+1]);
            Log.d(LOG_TAG, "lineDataPointArray[j] = " + lineDataPointArray[j].getX() + " : " + lineDataPointArray[j].getY() );
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(lineDataPointArray);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(6);
        series.setDrawAsPath(true);
        GraphView graphView = (GraphView) view.findViewById(R.id.graph);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(40);
        graphView.addSeries(series);*/
        //graphView.getViewport().setMaxXAxisSize(100);

        //LinearLayout layout = (LinearLayout) findViewById(R.id.graphLayout);
        //layout.addView(graphView);
        return view;
    }

    LineGraphSeries getBorderSeries() {
        LineGraphSeries borderSeries = new LineGraphSeries(
                new DataPoint[] {
                        new DataPoint(-graphPointsData[graphPointsData.length - 1]/1000, graphPointsData[graphPointsData.length - 1]),
                        new DataPoint(0, 0),
                        new DataPoint(graphPointsData[graphPointsData.length - 1]/1000, graphPointsData[graphPointsData.length - 1])
                }
        );
        Log.d(LOG_TAG, "graphPointsData[last element] = " +  graphPointsData[graphPointsData.length-1]);
        borderSeries.setDrawAsPath(true);
        borderSeries.setCustomPaint(getPaint());
        return borderSeries;
    }

    Paint getPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
        paint.setColor(Color.RED);
        return paint;
    }

    PointsGraphSeries getRoundSeries(int deviation) {
        DataPoint[] roundDataPoint = new DataPoint[4 * deviation];
        double x, y;
        for (int i = 0; i < deviation *2 + 1; i++) {
            x = i - deviation;
            y = Math.sqrt(Math.pow(deviation, 2) - Math.pow(x, 2));
            Log.d(LOG_TAG, "x, y = " + x + " : " + y);
            roundDataPoint[i] = new DataPoint(x , y);
            if(y > 0) {
                roundDataPoint[i + (deviation * 2)] = new DataPoint(x , -y);
            }
        }
        PointsGraphSeries roundGraphSeries = new PointsGraphSeries(roundDataPoint);
        roundGraphSeries.setColor(Color.RED);
        roundGraphSeries.setSize(5);
        return roundGraphSeries;
    }

}
