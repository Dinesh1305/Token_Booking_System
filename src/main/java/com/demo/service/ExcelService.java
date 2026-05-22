package com.demo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

    @Value("${excel.file.path}")
    private String filePath;

    public void addToExcel(String food, String email) {
        int cost = 0;

        switch (food) {
            case "Chicken Briyani": cost = 120; break;
            case "Egg Gravy": cost = 100; break;
            case "Chicken Gravy": cost = 100; break;
            case "Cauliflower Curry": cost = 40; break;
            case "Chicken 65": cost = 90; break;
            case "Bread Omelet": cost = 30; break;
            case "Boiled Egg": cost = 20; break;
            default: cost = 0; break;
        }

        File file = new File(filePath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            Workbook workbook;
            Sheet sheet;

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fis);
                }
                sheet = workbook.getSheetAt(0);
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Order Sheet");
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Email");
                headerRow.createCell(1).setCellValue("Total Cost");
            }

            boolean emailExists = false;
            int lastRowNum = sheet.getLastRowNum();

            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell emailCell = row.getCell(0);
                    if (emailCell != null && emailCell.getStringCellValue().equalsIgnoreCase(email)) {
                        Cell costCell = row.getCell(1);
                        double existingCost = costCell != null ? costCell.getNumericCellValue() : 0;
                        if (costCell == null) {
                            costCell = row.createCell(1);
                        }
                        costCell.setCellValue(existingCost + cost);
                        emailExists = true;
                        break;
                    }
                }
            }

            if (!emailExists) {
                Row newRow = sheet.createRow(lastRowNum + 1);
                newRow.createCell(0).setCellValue(email);
                newRow.createCell(1).setCellValue(cost);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            } finally {
                workbook.close();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}