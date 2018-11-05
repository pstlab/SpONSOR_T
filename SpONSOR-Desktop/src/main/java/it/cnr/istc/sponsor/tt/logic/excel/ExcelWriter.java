/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.sponsor.tt.logic.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import it.cnr.istc.sponsor.tt.logic.ParsedAccount;
import it.cnr.istc.sponsor.tt.logic.SettingsManager;
import it.cnr.istc.sponsor.tt.logic.TrainerManager;
import it.cnr.istc.sponsor.tt.logic.model.Account.Gender;
import it.cnr.istc.sponsor.tt.logic.model.Activity;
import it.cnr.istc.sponsor.tt.logic.model.ActivityTurn;
import it.cnr.istc.sponsor.tt.logic.model.ComfirmedTurn;
import it.cnr.istc.sponsor.tt.logic.model.Person;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class ExcelWriter {

    public static Map<Activity, List<ActivityTurn>> mapSolution(List<ActivityTurn> list) {
        Map<Activity, List<ActivityTurn>> map = new HashMap<>();

        System.out.println("====================== mapping =================");
        for (ActivityTurn turn : list) {
            Activity a = TrainerManager.getInstance().getActivityByTurn(turn);
            if (!map.containsKey(a)) {
                map.put(a, new ArrayList<ActivityTurn>());
            }
            map.get(a).add(turn);
            System.out.println("mapping: "+a.getActivityName().getName());
        }
        return map;
    }

    public static File writeSolution(List<ActivityTurn> lala, boolean isOneWeek) {

        Map<Activity, List<ActivityTurn>> mapSolution = mapSolution(lala);
        try {
            String filename = "plan_" + new Date().getTime() + ".xlsx";
            FileOutputStream outputStream = new FileOutputStream(filename);
            XSSFWorkbook workbook = new XSSFWorkbook();
            for (Activity a : mapSolution.keySet()) {
                List<ActivityTurn> list = mapSolution.get(a);
                Map<String, Integer> rowMap = new HashMap<>();
                Map<String, XSSFRichTextString> richMap = new HashMap<>();

                XSSFSheet sheet = workbook.createSheet(a.getActivityName().getName());
                sheet.setDefaultRowHeight((short)400);
                //JOptionPane.showMessageDialog(null, "A: "+a.getActivityName().getName());
                int openTime = SettingsManager.getInstance().getOpenTime();
                int closeTime = SettingsManager.getInstance().getCloseTime();
                int rr = closeTime - openTime;
                String[][] datatypes = new String[rr * 2 + 4][8];
                SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMMM-yyyy");

                datatypes[0][0] = "Orario";
                datatypes[0][1] = "Lunedì";
                datatypes[0][2] = "Martedì";
                datatypes[0][3] = "Mercoledì";
                datatypes[0][4] = "Giovedì";
                datatypes[0][5] = "Venerdì";
                datatypes[0][6] = "Sabato";
                datatypes[0][7] = "Domenica";

                for (int k = openTime, s = 0; k <= closeTime; k++, s++) {
                    System.out.println("inserting : " + "" + k + ":00" + " at " + (s * 2 + 1));
                    System.out.println("inserting : " + "" + k + ":30" + " at " + (s * 2 + 2));
                    datatypes[s * 2 + 1][0] = "" + k + ":00";
                    datatypes[s * 2 + 2][0] = "" + k + ":30";
                    rowMap.put(datatypes[s * 2 + 1][0], s * 2 + 1);
                    rowMap.put(datatypes[s * 2 + 2][0], s * 2 + 2);
                }

                if (isOneWeek) {
                    for (ActivityTurn activityTurn : list) {
                        List<ComfirmedTurn> comfirmedTurns = activityTurn.getComfirmedTurns();

                        Date startDate = new Date(activityTurn.getStartTime());
                        int column = startDate.getDay() == 0 ? 7 : startDate.getDay();
                        String ssTime = startDate.getHours() + ":" + (startDate.getMinutes() < 10 ? "0" + startDate.getMinutes() : startDate.getMinutes());

                        Date endDate = new Date(activityTurn.getEndTime());
                        String esTime = endDate.getHours() + ":" + (endDate.getMinutes() < 10 ? "0" + endDate.getMinutes() : endDate.getMinutes());

//                String people = "(" + ssTime + " - " + esTime + ")";
                        XSSFRichTextString rts = new XSSFRichTextString("");

                        XSSFFont fontBold = workbook.createFont();
                        fontBold.setBold(true); //set bold
                        fontBold.setFontHeight(12); //add font size

                        rts.append("(" + ssTime + " - " + esTime + ")\r\n", fontBold);

                        for (ComfirmedTurn ct : comfirmedTurns) {
//                    people += (ct.getPerson().toString() + "\r\n");
                            rts.append(ct.getPerson().toString() + "\r\n");
                        }
                        System.out.println("SSTIME = " + ssTime + "    ESTIME = " + esTime);
                        int startDayRow = rowMap.get(ssTime);
                        int endDayRow = rowMap.get(esTime);
                        datatypes[startDayRow][column] = rts.toString();
                        richMap.put(rts.toString(), rts);
                        if(startDayRow < endDayRow-1){
                            sheet.addMergedRegion(new CellRangeAddress(startDayRow, endDayRow - 1, column, column));
                        }
                        sheet.setColumnWidth(column, 5000);
                        sheet.autoSizeColumn(column, true);
                    }
                }
                System.out.println("======================================================");
                int rowNum = 0;
                System.out.println("Creating excel");
                for (int i = 0; i < datatypes.length; i++) {
                    Row row = sheet.createRow(rowNum++);

                    int colNum = 0;
                    for (int j = 0; j < datatypes[i].length; j++) {
                        Cell cell = row.createCell(colNum++);
                        if (i == 0) {
                            XSSFCellStyle title = workbook.createCellStyle();
//                greyBackgroundBold.setFont(font);
//                greyBackgroundBold.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
//                greyBackgroundBold.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//                    title.setFillBackgroundColor(new XSSFColor(new Color(153, 204, 255)));
//                    title.setFillBackgroundColor(IndexedColors.PALE_BLUE.getIndex());
                            if (j == 7) {
                                title.setFillForegroundColor(new XSSFColor(new Color(228, 40, 40)));
                            } else {
                                title.setFillForegroundColor(new XSSFColor(new Color(153, 204, 255)));
                            }
                            title.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            cell.setCellValue((String) datatypes[i][j]);
                            cell.setCellStyle(title);
                        }
                        if (datatypes[i][j] instanceof String && i != 0) {
                            XSSFCellStyle cs = workbook.createCellStyle();
                            cs.setWrapText(true);
                            if (richMap.containsKey(datatypes[i][j])) {
                                cell.setCellValue(richMap.get(datatypes[i][j]));
                            } else {
                                cell.setCellValue((String) datatypes[i][j]);
                            }

                            cell.setCellStyle(cs);
                        }
                        row.setHeight((short) -1);
                    }

                }

                System.out.println("Done");

            }
            workbook.write(outputStream);
            workbook.close();
            File file = new File(filename);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

//  
    public static File writeSolution2(List<ActivityTurn> list, boolean isOneWeek) {

        Map<String, Integer> rowMap = new HashMap<>();
        Map<String, XSSFRichTextString> richMap = new HashMap<>();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("plan");
        int openTime = SettingsManager.getInstance().getOpenTime();
        int closeTime = SettingsManager.getInstance().getCloseTime();
        int rr = closeTime - openTime;
        String[][] datatypes = new String[rr * 2 + 4][8];
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMMM-yyyy");

        datatypes[0][0] = "Orario";
        datatypes[0][1] = "Lunedì";
        datatypes[0][2] = "Martedì";
        datatypes[0][3] = "Mercoledì";
        datatypes[0][4] = "Giovedì";
        datatypes[0][5] = "Venerdì";
        datatypes[0][6] = "Sabato";
        datatypes[0][7] = "Domenica";

        for (int k = openTime, s = 0; k <= closeTime; k++, s++) {
            System.out.println("inserting : " + "" + k + ":00" + " at " + (s * 2 + 1));
            System.out.println("inserting : " + "" + k + ":30" + " at " + (s * 2 + 2));
            datatypes[s * 2 + 1][0] = "" + k + ":00";
            datatypes[s * 2 + 2][0] = "" + k + ":30";
            rowMap.put(datatypes[s * 2 + 1][0], s * 2 + 1);
            rowMap.put(datatypes[s * 2 + 2][0], s * 2 + 2);
        }

        if (isOneWeek) {
            for (ActivityTurn activityTurn : list) {
                List<ComfirmedTurn> comfirmedTurns = activityTurn.getComfirmedTurns();

                Date startDate = new Date(activityTurn.getStartTime());
                int column = startDate.getDay() == 0 ? 7 : startDate.getDay();
                String ssTime = startDate.getHours() + ":" + (startDate.getMinutes() < 10 ? "0" + startDate.getMinutes() : startDate.getMinutes());

                Date endDate = new Date(activityTurn.getEndTime());
                String esTime = endDate.getHours() + ":" + (endDate.getMinutes() < 10 ? "0" + endDate.getMinutes() : endDate.getMinutes());

//                String people = "(" + ssTime + " - " + esTime + ")";
                XSSFRichTextString rts = new XSSFRichTextString("");

                XSSFFont fontBold = workbook.createFont();
                fontBold.setBold(true); //set bold
                fontBold.setFontHeight(12); //add font size

                rts.append("(" + ssTime + " - " + esTime + ")\r\n", fontBold);

                for (ComfirmedTurn ct : comfirmedTurns) {
//                    people += (ct.getPerson().toString() + "\r\n");
                    rts.append(ct.getPerson().toString() + "\r\n");
                }
                System.out.println("SSTIME = " + ssTime + "    ESTIME = " + esTime);
                int startDayRow = rowMap.get(ssTime);
                int endDayRow = rowMap.get(esTime);
                datatypes[startDayRow][column] = rts.toString();
                richMap.put(rts.toString(), rts);

                sheet.addMergedRegion(new CellRangeAddress(startDayRow, endDayRow - 1, column, column));
                sheet.setColumnWidth(column, 5000);
                sheet.autoSizeColumn(column, true);
            }
        }
        System.out.println("======================================================");
        int rowNum = 0;
        System.out.println("Creating excel");
        for (int i = 0; i < datatypes.length; i++) {
            Row row = sheet.createRow(rowNum++);

            int colNum = 0;
            for (int j = 0; j < datatypes[i].length; j++) {
                Cell cell = row.createCell(colNum++);
                if (i == 0) {
                    XSSFCellStyle title = workbook.createCellStyle();
//                greyBackgroundBold.setFont(font);
//                greyBackgroundBold.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
//                greyBackgroundBold.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//                    title.setFillBackgroundColor(new XSSFColor(new Color(153, 204, 255)));
//                    title.setFillBackgroundColor(IndexedColors.PALE_BLUE.getIndex());
                    if (j == 7) {
                        title.setFillForegroundColor(new XSSFColor(new Color(228, 40, 40)));
                    } else {
                        title.setFillForegroundColor(new XSSFColor(new Color(153, 204, 255)));
                    }
                    title.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellValue((String) datatypes[i][j]);
                    cell.setCellStyle(title);
                }
                if (datatypes[i][j] instanceof String && i != 0) {
                    XSSFCellStyle cs = workbook.createCellStyle();
                    cs.setWrapText(true);
                    if (richMap.containsKey(datatypes[i][j])) {
                        cell.setCellValue(richMap.get(datatypes[i][j]));
                    } else {
                        cell.setCellValue((String) datatypes[i][j]);
                    }

                    cell.setCellStyle(cs);
                }
                row.setHeight((short) -1);
            }

        }

        try {
            String filename = "plan_" + new Date().getTime() + ".xlsx";
            FileOutputStream outputStream = new FileOutputStream(filename);
            workbook.write(outputStream);
            workbook.close();
            File file = new File(filename);
            System.out.println("Done");
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

//    private static final String FILE_NAME = "responso.xlsx";
    public static File write(List<ParsedAccount> accounts) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Datatypes in Java");
        String[][] datatypes = new String[accounts.size() + 1][14];
        datatypes[0][0] = "Nome";
        datatypes[0][1] = "Cognome";
        datatypes[0][2] = "Data di Nascita";
        datatypes[0][3] = "Sesso";
        datatypes[0][4] = "Concreto";
        datatypes[0][5] = "Presidente";
        datatypes[0][6] = "Strutturatore";
        datatypes[0][7] = "Geniale";
        datatypes[0][8] = "Esploratore";
        datatypes[0][9] = "Valutatore";
        datatypes[0][10] = "Lavoratore";
        datatypes[0][11] = "Obiettivista";
        datatypes[0][12] = "Dormiente";
        datatypes[0][13] = "Keywords";
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMMM-yyyy");

        for (int i = 0; i < accounts.size(); i++) {
            datatypes[i + 1][0] = accounts.get(i).getAccount().getName();
            datatypes[i + 1][1] = accounts.get(i).getAccount().getSurname();
            if (accounts.get(i).getAccount().getBornDate() == null) {
                datatypes[i + 1][2] = "non pervenuta";
            } else {
                datatypes[i + 1][2] = dt1.format(accounts.get(i).getAccount().getBornDate());
            }
            datatypes[i + 1][3] = accounts.get(i).getAccount().getGender() == Gender.MALE ? "Maschio" : "Femmina";
            datatypes[i + 1][4] = "" + accounts.get(i).getLeader();
            datatypes[i + 1][5] = "" + accounts.get(i).getConcreto();
            datatypes[i + 1][6] = "" + accounts.get(i).getLeader();
            datatypes[i + 1][7] = "" + accounts.get(i).getPianificatore();
            datatypes[i + 1][8] = "" + accounts.get(i).getBrillante();
            datatypes[i + 1][9] = "" + accounts.get(i).getEsploratore();
            datatypes[i + 1][10] = "" + accounts.get(i).getLavoratore();
            datatypes[i + 1][11] = "" + accounts.get(i).getOggettivo();
            datatypes[i + 1][12] = "" + (accounts.get(i).isSleeping() ? "YES" : "NO");
            Person personByID = TrainerManager.getInstance().getPersonByID(accounts.get(i).getAccount().getId());
            datatypes[i + 1][13] = "" + (personByID.getKeywords().stream().map(z -> z.getKeyword()).collect(Collectors.joining(", ")));
        }

        System.out.println("===================== VIEWING MATRIX =================");
        for (int i = 0; i < datatypes.length; i++) {
            for (int j = 0; j < datatypes[i].length; j++) {
                System.out.print(datatypes[i][j] + ", ");
            }
            System.out.println("");
        }
        System.out.println("======================================================");
        int rowNum = 0;
        System.out.println("Creating excel");

        for (int i = 0; i < datatypes.length; i++) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (int j = 0; j < datatypes[i].length; j++) {
                Cell cell = row.createCell(colNum++);
                if (datatypes[i][j] instanceof String) {
                    cell.setCellValue((String) datatypes[i][j]);
                }
                if (i == 0) {
                    XSSFCellStyle title = workbook.createCellStyle();
//                greyBackgroundBold.setFont(font);
//                greyBackgroundBold.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
//                greyBackgroundBold.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//                    title.setFillBackgroundColor(new XSSFColor(new Color(153, 204, 255)));
//                    title.setFillBackgroundColor(IndexedColors.PALE_BLUE.getIndex());
                    title.setFillForegroundColor(new XSSFColor(new Color(153, 204, 255)));
                    title.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(title);
                }
                if (j == 12) {
                    System.out.println("datatype[" + i + "][" + j + "] " + datatypes[i][1] + " ---> " + datatypes[i][j]);
                    XSSFCellStyle sfondo = workbook.createCellStyle();
                    if (((String) datatypes[i][j]).equals("YES")) {
                        sfondo.setFillForegroundColor(new XSSFColor(Color.GREEN));
                        sfondo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cell.setCellStyle(sfondo);
                    }
                    if (((String) datatypes[i][j]).equals("NO")) {
                        sfondo.setFillForegroundColor(new XSSFColor(Color.RED));
                        sfondo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cell.setCellStyle(sfondo);
                    }
                }
                if (j != 12 && ((String) datatypes[i][12]).equals("NO")) {
                    XSSFCellStyle sfondo = workbook.createCellStyle();
                    sfondo.setFillForegroundColor(new XSSFColor(Color.LIGHT_GRAY));
                    sfondo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(sfondo);
                }

            }
        }

        try {
            String filename = "responso" + new Date().getTime() + ".xlsx";
            FileOutputStream outputStream = new FileOutputStream(filename);
            workbook.write(outputStream);
            workbook.close();
            File file = new File(filename);
            System.out.println("Done");
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
