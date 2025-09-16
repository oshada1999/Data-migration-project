package com.cba.datamigration.controller;

import com.cba.datamigration.dto.ServiceAgreementDTO;
import com.cba.datamigration.mapper.ServiceAgreementRowMapper;
import com.cba.datamigration.model.SAModel;
import com.cba.datamigration.util.FileDataReader;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ServiceAgreementController {
    private FileDataReader fileDataReader;
    public ServiceAgreementController() {
        this.fileDataReader = new FileDataReader();
    }
    public void processData(File file, Consumer<Boolean> onCompletion) {
        System.out.println("Processing file : " + file.getName());
        List<ServiceAgreementDTO> saList = new ArrayList<>();
        boolean success = false;
        try {
            if (file.getName().endsWith(".csv")) {
                saList = fileDataReader.readCsv(file, new ServiceAgreementRowMapper());
            } else {
                saList = fileDataReader.readExcel(file, new ServiceAgreementRowMapper());
            }

            if (saList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Valid Data", "No data found in the file.", null);
                onCompletion.accept(true); // Indicate completion even if no data
                return;
            }

            SAModel model = new SAModel();

            int batchSize = 1000;
            int total = saList.size();
            int totalInserted = 0;

            for (int i = 0; i < total; i += batchSize) {
                int end = Math.min(i + batchSize, total);
                List<ServiceAgreementDTO> batch = saList.subList(i, end);

                model.saveWarrantyTemplate(batch);
                totalInserted += batch.size();
                System.out.println("Inserted batch " + ((i / batchSize) + 1) +
                        ": " + batch.size() + " rows, Total inserted so far: " + totalInserted);
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "SA records inserted in batches.", null);
            success = true;

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error processing file: " + e.getMessage(), null);
        } finally {
            onCompletion.accept(success);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
