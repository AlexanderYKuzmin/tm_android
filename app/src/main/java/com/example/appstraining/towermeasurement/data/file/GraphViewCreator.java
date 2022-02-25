package com.example.appstraining.towermeasurement.data.file;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.appstraining.towermeasurement.model.GraphicType;
import com.example.appstraining.towermeasurement.view.result.ReportPreparePresenter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphViewCreator {
    private Context context;
    private Map<GraphicType, int[]> pointsDataMap;
    private Map<GraphicType, int[]> rangeMap;
    private int deviation;

    private static final DataPoint[] lineDataPoint = new DataPoint[2];
    private static final DataPoint[] pDataPoint = new DataPoint[1];

    private final Map<GraphicType, GraphView> graphicViews = new HashMap<>();

    private static GraphViewCreator instance;

    private GraphViewCreator(Context context, @NonNull Map<GraphicType, int[]> pointsDataMap, @NonNull Map<GraphicType, int[]> rangeMap, int deviation) {
        this.context = context;
        this.pointsDataMap = pointsDataMap;
        this.rangeMap = rangeMap;
        this.deviation = deviation;
    }

    public static GraphViewCreator getInstance(Context context, @NonNull Map<GraphicType, int[]> pointsDataMap, @NonNull Map<GraphicType, int[]> rangeMap, int deviation) {
        if(instance == null) {
            instance = new GraphViewCreator(context, pointsDataMap, rangeMap, deviation);
        }
        return instance;
    }

    public void create(GraphicType graphicType) {
        GraphView graphView = new GraphView(context);
        graphView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);

        if(graphicType == GraphicType.XOZ || graphicType == GraphicType.YOZ) {

            graphView.getViewport().setMinX(rangeMap.get(graphicType)[ReportPreparePresenter.MIN_X] - 5);
            graphView.getViewport().setMaxX(rangeMap.get(graphicType)[ReportPreparePresenter.MAX_X] + 5);
            graphView.getViewport().setMinY(rangeMap.get(graphicType)[ReportPreparePresenter.MIN_Y]);
            graphView.getViewport().setMaxY(rangeMap.get(graphicType)[ReportPreparePresenter.MAX_Y]);

            graphView.getGridLabelRenderer().setNumVerticalLabels(
                    rangeMap.get(graphicType)[ReportPreparePresenter.MAX_Y]/1000);

            graphView.addSeries(getBorderSeries(pointsDataMap.get(graphicType)));

        } else {   // For XOY graphic

            if (rangeMap.get(graphicType)[ReportPreparePresenter.MIN_X]  < -deviation) {
                graphView.getViewport().setMinX(rangeMap.get(graphicType)[ReportPreparePresenter.MIN_X]);
            } else {
                graphView.getViewport().setMinX(-deviation - 5);
            }

            if (rangeMap.get(graphicType)[ReportPreparePresenter.MAX_X] > deviation) {
                graphView.getViewport().setMaxX(rangeMap.get(graphicType)[ReportPreparePresenter.MAX_X]);
            } else {
                graphView.getViewport().setMaxX(deviation + 5);
            }

            if (rangeMap.get(graphicType)[ReportPreparePresenter.MIN_Y] < -deviation) {
                graphView.getViewport().setMinY(rangeMap.get(graphicType)[ReportPreparePresenter.MIN_Y]);
            } else {
                graphView.getViewport().setMinY(-deviation - 5);
            }

            if (rangeMap.get(graphicType)[ReportPreparePresenter.MAX_Y] > deviation) {
                graphView.getViewport().setMaxY(rangeMap.get(graphicType)[ReportPreparePresenter.MAX_Y]);
            } else {
                graphView.getViewport().setMaxY(deviation + 5);
            }

            graphView.getGridLabelRenderer().setNumVerticalLabels(
                    rangeMap.get(graphicType)[ReportPreparePresenter.MAX_Y]/5);
            graphView.getGridLabelRenderer().setNumVerticalLabels(
                    rangeMap.get(graphicType)[ReportPreparePresenter.MAX_Y]/5);


            graphView.addSeries(getRoundSeries(deviation));
        }

        List<LineGraphSeries> lineSeriesList = getLineSeriesList(pointsDataMap.get(graphicType));
        for (LineGraphSeries s : lineSeriesList) {
            s.setColor(Color.BLUE);
            graphView.addSeries(s);
        }

        List<PointsGraphSeries> pointSeriesList = getPointsSeriesList(pointsDataMap.get(graphicType), graphicType, deviation);
        for(PointsGraphSeries s : pointSeriesList) {
            graphView.addSeries(s);
        }

        graphView.setTitle(graphicType.name());
        graphicViews.put(graphicType, graphView);
    }

    public List<LineGraphSeries> getLineSeriesList(int[] graphPointsData) {
        List<LineGraphSeries> lineSeriesList = new ArrayList<>();
        for (int i = 0; i < graphPointsData.length - 2; i += 2) {
            //pDataPoint[0] = new DataPoint(graphPointsData[i + 2], graphPointsData[i + 3]);
            if(graphPointsData[i] > graphPointsData[i + 2]) {
                lineDataPoint[0] = new DataPoint(graphPointsData[i + 2], graphPointsData[i + 3]);
                lineDataPoint[1] = new DataPoint(graphPointsData[i], graphPointsData[i+1]);
            } else {
                if (graphPointsData[i] == graphPointsData[i + 2]) graphPointsData[i + 2] += 0.01;
                lineDataPoint[0] = new DataPoint(graphPointsData[i], graphPointsData[i+1]);
                lineDataPoint[1] = new DataPoint(graphPointsData[i + 2], graphPointsData[i + 3]);
            }
            lineSeriesList.add(new LineGraphSeries(lineDataPoint));
        }
        return lineSeriesList;
    }

    public List<PointsGraphSeries> getPointsSeriesList(int[] graphPointsData, GraphicType graphicType, int deviation) {
        List<PointsGraphSeries> pointSeriesList = new ArrayList<>();
        for (int i = 0; i < graphPointsData.length - 2; i += 2) {
            pDataPoint[0] = new DataPoint(graphPointsData[i + 2], graphPointsData[i + 3]);
            PointsGraphSeries pointsGraphSeries = new PointsGraphSeries(pDataPoint);
            pointsGraphSeries.setSize(12);
            if(graphicType != GraphicType.XOY) {
                if(pDataPoint[0].getX() > pDataPoint[0].getY()/1000) {
                    pointsGraphSeries.setColor(Color.RED);
                }
            } else {
                if(Math.abs(pDataPoint[0].getX()) > deviation || Math.abs(pDataPoint[0].getY()) > deviation) {
                    pointsGraphSeries.setColor(Color.RED);
                }
            }
            pointSeriesList.add(pointsGraphSeries);
        }
        return pointSeriesList;
    }


    public LineGraphSeries getBorderSeries(int[] graphPointsData) {
        LineGraphSeries borderSeries = new LineGraphSeries(
                new DataPoint[] {
                        new DataPoint(-graphPointsData[graphPointsData.length - 1]/1000, graphPointsData[graphPointsData.length - 1]),
                        new DataPoint(0, 0),
                        new DataPoint(graphPointsData[graphPointsData.length - 1]/1000, graphPointsData[graphPointsData.length - 1])
                }
        );
        borderSeries.setDrawAsPath(true);
        borderSeries.setCustomPaint(getPaint());
        return borderSeries;
    }

    public  PointsGraphSeries getRoundSeries(int deviation) {
        DataPoint[] roundDataPoint = new DataPoint[4 * deviation];
        double x, y;
        for (int i = 0; i < deviation *2 + 1; i++) {
            x = i - deviation;
            y = Math.sqrt(Math.pow(deviation, 2) - Math.pow(x, 2));
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

    private static Paint getPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
        paint.setColor(Color.RED);
        return paint;
    }

    public GraphView get(GraphicType graphicType) {
        return graphicViews.get(graphicType);
    }
}
