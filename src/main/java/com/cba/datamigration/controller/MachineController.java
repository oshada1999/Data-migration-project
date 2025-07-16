package com.cba.datamigration.controller;

import com.cba.datamigration.dto.MachineDTO;
import com.cba.datamigration.mapper.MachineRowMapper;
import com.cba.datamigration.model.MachineModel;
import com.cba.datamigration.util.FileDataReader;
import javafx.scene.control.Alert; // Still needed for showing alerts from here

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer; // For callback

public class MachineController {

    private FileDataReader fileDataReader; // Inject or instantiate

    public MachineController() {
        this.fileDataReader = new FileDataReader(); // Or use dependency injection
    }

    public void processData(File file, Consumer<Boolean> onCompletion) {
        System.out.println("Processing file : " + file.getName());
        List<MachineDTO> machines = new ArrayList<>();
        boolean success = false;
        try {
            if (file.getName().endsWith(".csv")) {
                machines = fileDataReader.readCsv(file, new MachineRowMapper());
            } else {
                machines = fileDataReader.readExcel(file, new MachineRowMapper());
            }

            if (machines.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Valid Data", "No data found in the file.", null);
                onCompletion.accept(true); // Indicate completion even if no data
                return;
            }

            MachineModel model = new MachineModel();

            int batchSize = 1000;
            int total = machines.size();
            int totalInserted = 0;

            for (int i = 0; i < total; i += batchSize) {
                int end = Math.min(i + batchSize, total);
                List<MachineDTO> batch = machines.subList(i, end);

                model.saveMachineDataToTables(batch);
                totalInserted += batch.size();

                System.out.println("Inserted batch " + ((i / batchSize) + 1) +
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