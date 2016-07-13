package com.github.chm;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenhuaming on 16/7/13.
 */
public class Main {
    public static void main(String[] args) {
        InputStream in = Main.class.getClassLoader().getResourceAsStream("HFRZ_车款表最新.xlsx");

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            String s=  row.getCell(0).getStringCellValue();
            System.out.println(s);
            int rows = sheet.getLastRowNum();
            System.out.println(rows);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }finally{
            if(workbook!=null){
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Sheet sheet = workbook.getSheetAt(0);
    }
}
