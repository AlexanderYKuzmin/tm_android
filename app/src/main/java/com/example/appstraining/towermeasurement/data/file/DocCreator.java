package com.example.appstraining.towermeasurement.data.file;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.BaseOrTop;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.GraphicType;
import com.example.appstraining.towermeasurement.model.Result;
import com.example.appstraining.towermeasurement.util.DegreeNumericConverter;
import com.example.appstraining.towermeasurement.view.result.ReportPrepareActivity;
import com.example.appstraining.towermeasurement.view.result.ReportPreparePresenter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DocCreator {
    private final String LOG_TAG = "DocCreator";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final static int NUMBER_OF_TITLE_ROWS = 4;
    private final static int NUMBER_OF_LEVEL_ROWS = 4;
    private final static int NUMBER_OF_COLUMNS = 15;

    private final String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;

    private String[] title;
    private String[] commonData;
    private String[] tableTitleDataFirst;
    private String[] tableTitleDataSecond;
    private String[] headerDataSecondary;
    private String[] headerDataPrimary;

    public XWPFDocument document;

    public XWPFDocument getDocument() {
        return document;
    }

    public void saveDocument(String fileName) {
        FileLoader fileLoader = FileLoader.getInstance();
        fileLoader.saveReportDocx(fileName, document);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createDocFile(Building building) {

        initializeData(building);

        XWPFDocument document = new XWPFDocument();
        XWPFParagraph titleParagraph = document.createParagraph();
        XWPFParagraph commonDataParagraph = document.createParagraph();

        XWPFParagraph tableOneTitleParagraph = document.createParagraph();
        tableOneTitleParagraph.setIndentationRight(500);

        XWPFTable tableOne = document.createTable(2,15);
        tableOne.setWidth(9072);

        int tableBodyRows = (building.getNumberOfSections() + 1) * 4;   // create table dynamically!!!
        XWPFTable table1Body = document.createTable();
        table1Body.setWidth(9072);

        XWPFParagraph tableTwoTitleParagraph = document.createParagraph();
        tableTwoTitleParagraph.setIndentationRight(500);

        XWPFTable tableTwo = document.createTable(2,15);
        tableTwo.setWidth(9072);

        XWPFTable table2Body = document.createTable();
        table1Body.setWidth(9072);

        fillTitleParagraph(titleParagraph);
        fillParagraph(commonDataParagraph, commonData,12, false, ParagraphAlignment.LEFT);
        fillParagraph(commonDataParagraph, new String[] {""},12, false, ParagraphAlignment.LEFT);

        /*List<Measurement> measurementsForTabOne = building.getMeasurements().stream().filter(m -> m.getSide() == 1).collect(Collectors.toList());
        List<Result> resultsForTabOne = building.getResults().stream().filter(r -> measurementsForTabOne.co)*/
        List<Result> resultsForTabOne = filterResultsBySide(building, 1);
        List<Result> resultsForTabTwo = filterResultsBySide(building, 2);

        fillParagraph(tableOneTitleParagraph, tableTitleDataFirst,12, false, ParagraphAlignment.RIGHT);
        fillHeaderTable(tableOne);
        fillBodyTable(table1Body, resultsForTabOne, tableBodyRows);

        fillParagraph(tableTwoTitleParagraph, new String[] {"", "", "", ""},12, false, ParagraphAlignment.RIGHT);
        fillParagraph(tableTwoTitleParagraph, tableTitleDataSecond,12, false, ParagraphAlignment.RIGHT);
        fillHeaderTable(tableTwo);
        fillBodyTable(table2Body, resultsForTabTwo, tableBodyRows);

        this.document = document;
        /*return document;*/
    }



    public void addPicture (InputStream fis) throws IOException, InvalidFormatException {
        /*int byteSize = picture.getRowBytes() * picture.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);*/

        /*picture.copyPixelsToBuffer(byteBuffer);
        ByteArrayInputStream bis = new ByteArrayInputStream(byteBuffer.array());*/

        XWPFParagraph picParagraph = document.createParagraph();
        //fillPictureParagraph(picParagraph, fis);

    }

    public void addPictures(String[] fileNames) {
        fillPictureParagraph(document.createParagraph(), fileNames);
    }

    private void fillTitleParagraph(XWPFParagraph paragraph) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(16);
        run.setFontFamily("Times New Roman");
        run.setBold(true);
        run.setText(title[0]);
        run.addBreak();
    }

    private void fillPictureParagraph(XWPFParagraph picParagraph, String[] fileNames) {

        XWPFRun run = picParagraph.createRun();
        int x = 200, y = 320;
        //System.out.println("bis available" + bis.available());
        run.addBreak();
        for (int i = 0; i < fileNames.length; i++) {
            if (i == 2) {
                run.addBreak();
                run.addBreak();
                x = 250;
                y = 300;
            }
            try(FileInputStream fis = new FileInputStream(filePath + fileNames[i])) {
                run.addPicture(fis, Document.PICTURE_TYPE_PNG, fileNames[i], Units.toEMU(x), Units.toEMU(y));

                System.out.println("picture added");
            } catch( IOException | InvalidFormatException e) {
                e.printStackTrace();
            }
        }
        picParagraph.setAlignment(ParagraphAlignment.CENTER);
    }

    private void fillHeaderTable(XWPFTable table) {
        XWPFTableRow firstRow = table.getRows().get(0);
        firstRow.setHeight(400);

        XWPFTableRow secondRow = table.getRows().get(1);
        secondRow.setHeight(400);

        mergeCellVertically(table, 0, 0, 1);
        mergeCellVertically(table, 1, 0, 1);
        mergeCellVertically(table, 2, 0, 1);

        mergeCellHorizontally(table, 0, 4, 6);
        mergeCellHorizontally(table, 0, 6, 8);
        mergeCellHorizontally(table, 0, 7, 9);

        removeExceededCells(table, firstRow, 6);

        fillRow(firstRow, headerDataPrimary, 0, 8, true);
        fillRow(secondRow, headerDataSecondary, 3, 14,true);

        firstRow.getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT);
        secondRow.getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT);

        setTableColumnsWidth(firstRow);
        /*setTableColumnsWidth(table, 500, 500, 600, 500,
                200, 200, 200,
                200,
                200, 200, 200,
                200, 200, 200,
                2200);*/

    }

    private void fillBodyTable(XWPFTable table, List<Result> results, int tableBodyRows) {
        int levels = results.size();
        for (int cellNum = 0; cellNum < 14; cellNum ++) {
            table.getRow(0).createCell();
        }

        table.getRow(0).setHeight(300);
        table.getRow(0).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT);

        for (int i = 0; i < levels; i++) {
            createBlockFourLines(i, table);
            fillBlockFourLines(table, i, results.get(i));
        }
    }

    private void fillParagraph(XWPFParagraph paragraph, String[] text, int fontSize, boolean bold, ParagraphAlignment alignment) {
        paragraph.setAlignment(alignment);
        paragraph.setSpacingBefore(80);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(fontSize);
        run.setFontFamily("Times New Roman");
        run.setBold(bold);
        for (int i = 0; i < text.length; i++) {
            run.setText(text[i]);
            if( i != text.length -1) {
                run.addBreak();
            }
        }
    }

    private void fillRow(XWPFTableRow row, String[] cellsData, int from, int to, boolean isTitle) {
        List<XWPFTableCell> cellsList = row.getTableCells();
        //System.out.println(cellsList.size());

        //int spacing = 10;
        for(int i = from; i < to + 1; i++) {
            //System.out.println(cellsData[i]);

            if (cellsData[i] != null) {
                cellsList.get(i).removeParagraph(0);

                fillParagraph(cellsList.get(i).addParagraph(), new String[]{cellsData[i]}, 10, isTitle, ParagraphAlignment.CENTER);
                cellsList.get(i).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
            }
        }
        System.out.println();
    }

    void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for(int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if(rowIndex == fromRow){
                // The first merged cell is set with RESTART merge value
                vmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                vmerge.setVal(STMerge.CONTINUE);
                // and the content should be removed
                for (int i = cell.getParagraphs().size(); i > 0; i--) {
                    cell.removeParagraph(0);
                }
                cell.addParagraph();
            }
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr == null) tcPr = cell.getCTTc().addNewTcPr();
            tcPr.setVMerge(vmerge);
        }
    }

    void mergeCellHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
        XWPFTableCell cell = table.getRow(row).getCell(fromCol);
        // Try getting the TcPr. Not simply setting an new one every time.
        CTTcPr tcPr = cell.getCTTc().getTcPr();
        if (tcPr == null) {
            tcPr = cell.getCTTc().addNewTcPr();
        }
        // The first merged cell has grid span property set
        if (tcPr.isSetGridSpan()) {
            tcPr.getGridSpan().setVal(BigInteger.valueOf(toCol-fromCol+1));
        } else {
            tcPr.addNewGridSpan().setVal(BigInteger.valueOf(toCol-fromCol+1));
        }
        // Cells which join (merge) the first one, must be removed
        /*for(int colIndex = toCol; colIndex > fromCol; colIndex--) {
            table.getRow(row).getCtRow().removeTc(colIndex);
        }*/
    }

    private void removeExceededCells(XWPFTable table, XWPFTableRow row, int cellQuantity) {
        for(int colIndex = row.getTableCells().size() - 1; colIndex > row.getTableCells().size() - 1 - cellQuantity; colIndex--) {
            //System.out.println("coIndex = " + colIndex);
            //table.getRow(row).getCtRow().removeTc(colIndex);
            row.getCtRow().removeTc(colIndex);
            //row.removeCell(colIndex);
        }
    }

    private void createBlockFourLines(int i, XWPFTable table) {

        for (int j = i == 0 ? 1 : 0; j < 4; j++) {

            XWPFTableRow row = table.createRow();
            table.getRow(i * 4 + j).setHeight(300);
            table.getRow(i * 4 + j).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT);
            /*for (int cellNum = 0; cellNum < 14; cellNum ++) {
                row.createCell();
            }*/

            //System.out.println("i = " + i + " j = " + j);
        }
        int k = i * 4; // koef for the next block rows
        System.out.println("k = " + k);
        mergeCellVertically(table, 0, 0 + k, 3 + k);
        mergeCellVertically(table, 1, 0 + k, 3 + k);
        mergeCellVertically(table, 2, 0 + k, 1 + k);
        mergeCellVertically(table, 2, 2 + k, 3 + k);

        mergeCellVertically(table, 7, 0 + k, 1 + k);
        mergeCellVertically(table, 7, 2 + k, 3 + k);

        mergeCellVertically(table, 8, 0 + k,1 + k);
        mergeCellVertically(table, 8, 2 + k,3 + k);
        mergeCellVertically(table, 9, 0 + k,1 + k);
        mergeCellVertically(table, 9, 2 + k,3 + k);
        mergeCellVertically(table, 10, 0 + k,1 + k);
        mergeCellVertically(table, 10, 2 + k,3 + k);

        mergeCellVertically(table,11, 0 + k,3 + k);
        mergeCellVertically(table,12, 0 + k,3 + k);
        mergeCellVertically(table,13, 0 + k,3 + k);
        mergeCellVertically(table,14, 0 + k,3 + k);

        //table.setBottomBorder(XWPFTable.XWPFBorderType.THICK, 16, 0, "000000");
    }

    private void fillBlockFourLines(XWPFTable table, int level, Result result){
        int k = level * 4; // start row to fill

        int[] betaAverageLeft = DegreeNumericConverter.fromDecToDeg(result.getBetaAverageLeft());
        int[] betaAverageRight = DegreeNumericConverter.fromDecToDeg(result.getBetaAverageRight());
        int[] betaI = DegreeNumericConverter.fromDecToDeg(result.getBetaI());
        int betaDelta = (int) result.getBetaDelta();

        String[][] rowsData = new String[][] {
                new String[] {
                        String.valueOf(level),
                        String.valueOf(level * 8),
                        "Левый",
                        "KL",
                        String.valueOf(betaAverageLeft[0]), String.valueOf(betaAverageLeft[1]), String.valueOf(betaAverageLeft[2]),
                        "0",
                        String.valueOf(betaAverageLeft[0]), String.valueOf(betaAverageLeft[1]), String.valueOf(betaAverageLeft[2]),
                        String.valueOf(betaI[0]), String.valueOf(betaI[1]), String.valueOf(betaI[2]),
                        String.valueOf(betaDelta)
                },
                new String[] {
                        null,
                        null,
                        null,
                        "KR",
                        String.valueOf(betaAverageLeft[0]), String.valueOf(betaAverageLeft[1]), String.valueOf(betaAverageLeft[2]), // + 180 deg needed
                        null,
                        null, null, null,
                        null, null, null,
                        null
                },
                new String[] {
                        null,
                        null,
                        "Правый",
                        "KL",
                        String.valueOf(betaAverageRight[0]), String.valueOf(betaAverageRight[1]), String.valueOf(betaAverageRight[2]),
                        "0",
                        String.valueOf(betaAverageRight[0]), String.valueOf(betaAverageRight[1]), String.valueOf(betaAverageRight[2]),
                        null, null, null,
                        null
                },
                new String[] {
                        null,
                        null,
                        null,
                        "KR",
                        String.valueOf(betaAverageRight[0]), String.valueOf(betaAverageRight[1]), String.valueOf(betaAverageRight[2]),
                        null,
                        null, null, null,
                        null, null, null,
                        null
                }
        };
        for (int n = k; n < k + 4; n++) {
            List<XWPFTableCell> cells = table.getRow(n).getTableCells();
            //final int rowNum = n;
            fillRow(table.getRow(n), rowsData[n - k], 0, cells.size() - 1, false);
        }

    }

    private void setTableColumnsWidth(XWPFTableRow row) {
        row.getCell(0).setWidth("8%");
        row.getCell(1).setWidth("8%");
        row.getCell(2).setWidth("10%");
        row.getCell(3).setWidth("10%");
        row.getCell(4).setWidth("15%");
        row.getCell(5).setWidth("10%");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("DefaultLocale")
    private void initializeData(Building building) {
        title = new String[] {"Журнал угловых измерений"};

        String typeStringRu = null;
        commonData = new String[] {
                String.format("Адрес: %s", building.getAddress()),
                String.format("Тип АМС: %s", building.getType().getBuildingTypeRu()),
                String.format("Высота АМС: %d метров", building.getHeight() / 1000),
                String.format("Тип секции: %d гранная", building.getConfig()),
                String.format("Дата: %s", dateFormat.format(building.getMeasurement(1, 1, BaseOrTop.BASE).getDate())),
                String.format("Ветер: %s", "1-3 м/с"),
                String.format("Инструмент: %s", "(Средство измерения)")
        };

        int distanceOne = building.getMeasurements().stream().filter(m -> m.getSide() == 1).findFirst().get().getDistance() / 1000;
        tableTitleDataFirst = new String[] {
                String.format("Стоянка: %s", "1"),
                String.format("Расстояние до опоры, м: %d", distanceOne)
        };

        int distanceTwo = building.getMeasurements().stream().filter(m -> m.getSide() == 2).findFirst().get().getDistance() / 1000;
        tableTitleDataSecond = new String[] {
                String.format("Стоянка: %s", "2"),
                String.format("Расстояние до опоры, м: %d", distanceTwo)
        };

        headerDataPrimary = new String[] {
                "№пп",
                "Н, м",
                "Пояс",
                "Круг",
                "\u03B2мзм",
                "KL-KR",
                "\u03B2ср",
                "\u03B2i",
                "\u0394\u03B2"
        };

        headerDataSecondary = new String[]{
                "",
                "",
                "",
                "",
                "\u00B0",
                "\'",
                "\"",
                "",
                "\u00B0",
                "\'",
                "\"",
                "\u00B0",
                "\'",
                "\"",
                "\""
        };
    }

    private List<Result> filterResultsBySide(Building building, int side) {

        List<Result> results = building.getResults();
        List<Result> filteredResults = new ArrayList<>();

        int startIndex = results.size() / 2 * (side - 1);
        int endIndex = results.size() / 2 + startIndex - 1;
        for (int i = startIndex; i < endIndex + 1; i++) {
            filteredResults.add(results.get(i));
        }
        return filteredResults;
    }
}
