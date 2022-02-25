package com.example.appstraining.towermeasurement.file;

import static androidx.test.InstrumentationRegistry.getContext;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.appstraining.towermeasurement.TestData;
import com.example.appstraining.towermeasurement.data.file.DocCreator;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.GraphicType;
import com.example.appstraining.towermeasurement.util.BitmapConverter;
import com.example.appstraining.towermeasurement.view.main.MainActivity;
import com.example.appstraining.towermeasurement.view.main.MainView;
import com.example.appstraining.towermeasurement.view.result.ReportPrepareActivity;
import com.example.appstraining.towermeasurement.view.result.ReportPreparePresenter;
import com.jjoe64.graphview.GraphView;
import androidx.test.core.app.ApplicationProvider.*;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.File;
import java.io.FileOutputStream;

@RunWith(RobolectricTestRunner.class)
public class DocCreatorTest {

    private final static String docFilePath = "C:\\Users\\kuzmi\\IdeaProjects\\doc_file_studio.docx";
    private ReportPreparePresenter presenter;
    private int[] xozRange = {0, 0, 0, 0};
    private int[] yozRange = {0, 0, 0, 0};
    private int[] xoyRange = {0, 0, 0, 0};
    Configuration config;
    Context context;

    @Before
    public void setUp() {
        //ReportPrepareActivity activity = Robolectric.setupActivity(ReportPrepareActivity.class);

        presenter = new ReportPreparePresenter(TestData.getTestBuilding(),TestData.levels);
        //config = getResources().getConfiguration();
        //context = getApplicationContext<LocationTrackerApplication>();
        context = ApplicationProvider.getApplicationContext();
    }

   /* @Test
    public void createDocFileTest() {
        File file = new File(docFilePath);
        DocCreator docCreator = new DocCreator();
        Building building = TestData.getTestBuilding();

        docCreator.createDocFile(building);
        int[] xozArray = presenter.getXOZArray();
        GraphView graphView = docCreator.createGraphView(
                context,
                xozArray,
                xozRange,
                GraphicType.XOZ,
                xozArray[xozArray.length - 1] / 1000
                );



        try {
            docCreator.addPicture(BitmapConverter.getBitmapFromView(graphView));
            FileOutputStream fos = new FileOutputStream(file);
            docCreator.getDocument().write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
