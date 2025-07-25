package com.cba.datamigration.controller;

import com.cba.datamigration.dto.ChildDTO;
import com.cba.datamigration.dto.MainClientDTO;
import com.cba.datamigration.mapper.ChildRowMapper;
import com.cba.datamigration.mapper.MainClientRowMapper;
import com.cba.datamigration.model.ChildModel;
import com.cba.datamigration.model.MainClientModel;
import com.cba.datamigration.util.FileDataReader;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChildController {
    private FileDataReader fileDataReader; // Inject or instantiate

    public ChildController() {
        this.fileDataReader = new FileDataReader(); // Or use dependency injection
    }

    public void processData(File file, Consumer<Boolean> onCompletion) {
        System.out.println("Processing file : " + file.getName());
        List<ChildDTO> customers = new ArrayList<>();
        boolean success = false;
        try {
            if (file.getName().endsWith(".csv")) {
                customers = fileDataReader.readCsv(file, new ChildRowMapper());
            } else {
                customers = fileDataReader.readExcel(file, new ChildRowMapper());
            }

            if (customers.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Valid Data", "No data found in the file.", null);
                onCompletion.accept(true); // Indicate completion even if no data
                return;
            }

            ChildModel childModel = new ChildModel();

            int batchSize = 1000;
            int total = customers.size();
            int totalInserted = 0;

            for (int i = 0; i < total; i += batchSize) {
                int end = Math.min(i + batchSize, total);
                List<ChildDTO> batch = customers.subList(i, end);

                childModel.saveChildDataToTable(batch);
                totalInserted += batch.size();

                System.out.println("Inserted batch " + ((i / batchSize) + 1) +
                        ": " + batch.size() + " rows, Total inserted so far: " + totalInserted);
            }

            for (int i = 0; i < total; i += batchSize) {
                int end = Math.min(i + batchSize, total);
                List<ChildDTO> batch = customers.subList(i, end);

                childModel.phoneDataToTable(batch);
                totalInserted += batch.size();

                System.out.println("Inserted phone " + ((i / batchSize) + 1) +
                        ": " + batch.size() + " rows, Total inserted so far: " + totalInserted);
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Machine records inserted in batches.", null);
            success = true;

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error processing file: " + e.getMessage(), null);
        } finally {
            onCompletion.accept(success); // Notify MainController about completion status
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
