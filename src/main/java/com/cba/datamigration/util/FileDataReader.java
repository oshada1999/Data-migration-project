package com.cba.datamigration.util;

import com.cba.datamigration.mapper.RowMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDataReader {

    // original

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

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            boolean isFirstRow = true;
            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false; // skip header
                    continue;
                }

                String[] tokens = new String[row.getLastCellNum()];

                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);

                    if (cell == null) {
                        tokens[i] = "";
                        continue;
                    }

                    switch (cell.getCellType()) {
                        case STRING:
                            tokens[i] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            tokens[i] = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            tokens[i] = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            switch (cell.getCachedFormulaResultType()) {
                                case STRING:
                                    tokens[i] = cell.getStringCellValue();
                                    break;
                                case NUMERIC:
                                    tokens[i] = String.valueOf(cell.getNumericCellValue());
                                    break;
                                case BOOLEAN:
                                    tokens[i] = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case ERROR:
                                    tokens[i] = "ERROR"; // or "" or your default
                                    break;
                                default:
                                    tokens[i] = "";
                            }
                            break;
                        case ERROR:
                            tokens[i] = "ERROR"; // or "" or your default
                            break;
                        default:
                            tokens[i] = "";
                            break;
                    }
                }


                data.add(mapper.mapRow(tokens));
            }
        }

        return data;
    }

}
