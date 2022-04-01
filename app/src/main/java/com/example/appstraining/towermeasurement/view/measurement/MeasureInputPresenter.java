package com.example.appstraining.towermeasurement.view.measurement;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.DegreeSeparated;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.util.DegreeNumericConverter;
import com.example.appstraining.towermeasurement.util.MeasurementsValidationData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static com.example.appstraining.towermeasurement.util.DegreeNumericConverter.fromDecToDeg;
import static com.example.appstraining.towermeasurement.view.measurement.MeasureInputActivity.LEFT_LIST;

import androidx.annotation.RequiresApi;

public class MeasureInputPresenter {
    private final String LOG_TAG = "MeasureInputPresenter";
    private Context context;
    private MeasureInput mMeasureInputActivity;

    private ArrayList<Measurement> measurements;
    private List<Measurement> measurementsGroup1;
    private List<Measurement> measurementsGroup2;

    private int widthBottom;
    private int config;

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    public MeasureInputPresenter(Context context, MeasureInput measureInputActivity){
        this.context = context;
        mMeasureInputActivity = measureInputActivity;

    }

    public void sendValues(ArrayList<Measurement> measurements) {

    }

    public void setMeasurements(ArrayList<Measurement> measurements){
        Log.d(LOG_TAG, "setting measurements: size = " + measurements.size() + ";  date from 0 pos = " + measurements.get(0).getDate());
        this.measurements = measurements;
        /*measurementsGroup1 = measurements.stream().filter(measurement -> measurement.getSide() == 1).collect(Collectors.toList());
        measurementsGroup2 = measurements.stream().filter(measurement -> measurement.getSide() == 2).collect(Collectors.toList());*/
    }

    public void updateMeasurement(int measureNum, double leftAngle, double rightAngle, int azimuth,
                                  int theoDistance, int theoHeight) {
        Log.d(LOG_TAG, "Update measurement started.");
        final String DEFAULT_DATE = "01-01-2001";
        final String REGEX_DATE = "([1-3][0-9]-){2}20\\d{2}";

        int side = measurements.get(measureNum - 1).getSide();
        int sectionNumber = measurements.get(measureNum - 1).getSectionNumber();
        Measurement updateMeasurement = new Measurement(0, measureNum, side, null, leftAngle, rightAngle,
                theoHeight, theoDistance, sectionNumber, null, 0, 0, null, null, azimuth);

        if (MeasurementsValidationData.isMeasurementConsistent(updateMeasurement, widthBottom, config)) {

        /*String contractor = params[0];
        String dateStr = params[1];*/
            //int theoHeight = t;
            //int distance = Integer.parseInt(params[2]);

        /*Pattern datePattern = Pattern.compile(REGEX_DATE);
        Matcher matcher = datePattern.matcher(dateStr);

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        java.util.Date date = null;
        if(!matcher.matches()) { dateStr = DEFAULT_DATE; }
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            Log.d(LOG_TAG, "Parsing date is not succeed.");
            e.printStackTrace();
        }*/

            //java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            Measurement measurement = measurements.get(measureNum - 1);
            measurement.setLeftAngle(leftAngle);
            measurement.setRightAngle(rightAngle);
            measurement.setTheoHeight(theoHeight);
            measurement.setDistance(theoDistance); // distance to axis building
            //measurement.setDate(sqlDate);
            //measurement.setContractor(contractor);
            measurement.setAzimuth(azimuth);
            Log.d(LOG_TAG, "Measurements are updated. MeasureNum = ");
            int[] leftAngleTest = DegreeNumericConverter.fromDecToDeg(measurement.getLeftAngle());
            Log.d(LOG_TAG, "left angle: "
                    + leftAngleTest[0] + " "
                    + leftAngleTest[1] + " "
                    + leftAngleTest[2]
            );
            mMeasureInputActivity.getFragment(measurement.getSide())
                    .onItemUpdateMeasureList(measurement, new DegreeSeparated(
                            DegreeNumericConverter.fromDecToDeg(measurement.getLeftAngle()),
                            DegreeNumericConverter.fromDecToDeg(measurement.getRightAngle()))
                    );
        } else {
            Toast.makeText(context, "Текущее измерение некорректно!", Toast.LENGTH_LONG).show();
        }

    }

    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Measurement> getMeasurementGroup(int group) {
        switch (group) {
            case 1: return (ArrayList<Measurement>) measurements.stream().filter(measurement -> measurement.getSide() == 1).collect(Collectors.toList());
            case 2: return (ArrayList<Measurement>) measurements.stream().filter(measurement -> measurement.getSide() == 2).collect(Collectors.toList());
            default: return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<DegreeSeparated> getDegreeSeparatedAngleListByGroup(int group) {
        ArrayList<DegreeSeparated> degreeSeparatedList = new ArrayList<>();
        Log.d(LOG_TAG, "measurements size = " + measurements.size());
        List<Measurement> measurementsGroup = measurements.stream().filter(m -> m.getSide() == group)
                .sorted(Comparator.comparing(Measurement::getNumber)).collect(Collectors.toList());
        Log.d(LOG_TAG, "measurementsGroup size = " + measurementsGroup.size());
        for (Measurement m: measurementsGroup) {
            int[] leftAngle = DegreeNumericConverter.fromDecToDeg(m.getLeftAngle());
            int[] rightAngle = DegreeNumericConverter.fromDecToDeg(m.getRightAngle());
            DegreeSeparated degreeSeparated = new DegreeSeparated(leftAngle[0],leftAngle[1], leftAngle[2],
                    rightAngle[0], rightAngle[1], rightAngle[2]);
            Log.d(LOG_TAG, "degreeSeparated = " + degreeSeparated.toString());
            degreeSeparatedList.add(degreeSeparated);
        }
        return degreeSeparatedList;
    }

    public ArrayList<int[]> getDegMinSecArrList(int sideDirection) { // {degree, minutes, seconds}
        ArrayList<int[]> degArrayList = new ArrayList<>();
        double dec = 0.0;
        for(int i = 0; i < measurements.size(); i++) {
            if (sideDirection == LEFT_LIST) {
                dec = measurements.get(i).getLeftAngle();
            } else {
                dec = measurements.get(i).getRightAngle();
            }
            degArrayList.add(fromDecToDeg(dec));
        }
        return degArrayList;
    }

    public ArrayList<int[]> getDegMinSecArrListByGroup(int sideDirection, int group) { // {degree, minutes, seconds}
        ArrayList<int[]> degArrayList = new ArrayList<>();
        ArrayList<Measurement> measurementsGroup;
        switch (group) {
            case 1:
                measurementsGroup = (ArrayList<Measurement>) measurementsGroup1;
                break;
            case 2: measurementsGroup = (ArrayList<Measurement>) measurementsGroup2;
                break;
            default: measurementsGroup = new ArrayList<>();
        }

        double dec = 0.0;
        for(int i = 0; i < measurementsGroup.size(); i++) {
            if (sideDirection == LEFT_LIST) {
                dec = measurementsGroup.get(i).getLeftAngle();
            } else {
                dec = measurementsGroup.get(i).getRightAngle();
            }
            degArrayList.add(fromDecToDeg(dec));
        }
        return degArrayList;
    }

    public boolean isConstantsCorrect(String... params) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] getSingleMeasurementData(int position, int group) {
        Measurement measurement = getMeasurementGroup(group).get(position);
        int[] leftAngleDeg = fromDecToDeg(measurement.getLeftAngle());
        int[] rightAngleDeg = fromDecToDeg(measurement.getRightAngle());

        return  new int[]{
                leftAngleDeg[0],
                leftAngleDeg[1],
                leftAngleDeg[2],
                rightAngleDeg[0],
                rightAngleDeg[1],
                rightAngleDeg[2],
                measurement.getAzimuth(),
                measurement.getDistance(),
                measurement.getTheoHeight(),
                measurement.getNumber()
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDhaToMeasurements(int group, int[] dhaValues){
        getMeasurements().stream()
            .filter(measurement -> measurement.getSide() == group)
            .map(measurement -> {
                measurement.setDistance(dhaValues[0]);
                measurement.setTheoHeight(dhaValues[1]);
                measurement.setAzimuth(dhaValues[2]);
                return measurement;
            })
            .collect(Collectors.toList());

        Log.d(LOG_TAG, "size of Measurements: " + getMeasurements().size());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setCommonConstantsToMeasurements(String[] constants) {
        try {
            String contractor = constants[0];
            Date date = dateFormat.parse(constants[1]);
            Log.d(LOG_TAG, "util date is: " + date.toString());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            Log.d(LOG_TAG, "sql date is: " + sqlDate);

            getMeasurements().forEach(measurement -> {
                measurement.setContractor(contractor);
                measurement.setDate(sqlDate);
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "The SQL date from measurement is: " + getMeasurements().get(0).getDate());
    }

    public int getWidthBottom() {
        return widthBottom;
    }

    public void setWidthBottom(int widthBottom) {
        this.widthBottom = widthBottom;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }
}
