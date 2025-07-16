package com.cba.datamigration.util;

import com.cba.datamigration.mapper.RowMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDataReader {

    public <T> List<T> readCsv(File file, RowMapper<T> mapper) throws IOException {
        List<T> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            reader.skip(1); // Skip header
            String[] tokens;
            while ((tokens = reader.readNext()) != null) {
                data.add(mapper.mapRow(tokens));
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public <T> List<T> readExcel(File file, RowMapper<T> mapper) throws IOException {
        List<T> data = new ArrayList<>();
        try (InputStream is = new FileInputStream(file)) {
            Workbook workbook = file.getName().endsWith(".xlsx") ? new XSSFWorkbook(is) : new HSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;
            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }
                String[] tokens = new String[row.getLastCellNum()];
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    tokens[i] = cell == null ? "" : cell.toString();
                }
                data.add(mapper.mapRow(tokens));
            }
        }
        return data;
    }
}
