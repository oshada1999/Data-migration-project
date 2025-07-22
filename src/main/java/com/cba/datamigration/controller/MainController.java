package com.cba.datamigration.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

public class MainController {

    @FXML
    private Button btnUpload;

    @FXML
    private Button btnProcess;

    @FXML
    private ComboBox<String> cmbDataType;

    @FXML
    private Button btnDelete;

    @FXML
    private TextArea txtUpload;

    private File selectedFile;

    private int selectedIndex = -1;

    MachineController machineController = new MachineController();
    MainClientController mainClientController = new MainClientController(); // Assuming this exists
    ChildController childController = new ChildController();
    ChilCustomerPhone chilCustomerPhone = new ChilCustomerPhone();

    public void initialize() {
        btnProcess.setDisable(true);
        btnDelete.setDisable(true);
        cmbDataType.setDisable(true);

        ObservableList<String> dataTypes = FXCollections.observableArrayList(
                "01. Machine(category,brand,model)", "02. Parent Customer", "03. Child Customer"
        );
        cmbDataType.setItems(dataTypes);
    }

    @FXML
    void btnUploadOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Upload");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        Stage stage = (Stage) btnUpload.getScene().getWindow();
        File chosenFile = fileChooser.showOpenDialog(stage);

        if (chosenFile != null) {
            this.selectedFile = chosenFile;
            txtUpload.setText("Processing file : " + selectedFile.getName());
            cmbDataType.setDisable(false);
            btnDelete.setDisable(false);
            btnUpload.setDisable(true);
        } else {
            txtUpload.setText("");
            btnProcess.setDisable(true);
            btnDelete.setDisable(true);
            btnUpload.setDisable(false);
        }
    }

    @FXML
    void btnProcessOnAction(ActionEvent event) {
        if (selectedFile != null) {
            if (selectedIndex >= 0) {
                // Disable buttons while processing
                btnProcess.setDisable(true);
                btnDelete.setDisable(true);

                // Define a callback for when processing is complete
                Consumer<Boolean> onCompletion = success -> {
                    btnProcess.setDisable(false); // Re-enable process button
                    btnDelete.setDisable(false);  // Re-enable delete button
                };

                switch (selectedIndex) {
                    case 0:
                        machineController.processData(selectedFile, onCompletion);
                        break;
                    case 1:
                        mainClientController.processData(selectedFile, onCompletion);
                        break;
                    case 2:
                        childController.processData(selectedFile, onCompletion);
                        break;


                    default:
                        showAlert(Alert.AlertType.WARNING, "Invalid Data Type", "Selected data type is not supported.", null);
                        onCompletion.accept(false);
                        break;
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selected Data Type", "Please Select Data Type.", null);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No File Selected", "Please choose a file first.", null);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        this.selectedFile = null;
        txtUpload.setText("");
        btnUpload.setDisable(false);
        cmbDataType.setDisable(true);
        btnProcess.setDisable(true);
        btnDelete.setDisable(true);
        showAlert(Alert.AlertType.INFORMATION, "File Cleared", "Selected file has been removed.", null);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void selectOnAction(ActionEvent actionEvent) {
        selectedIndex = cmbDataType.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            btnProcess.setDisable(false);
            System.out.println("Selected Index: " + selectedIndex);
            System.out.println("Selected Value: " + cmbDataType.getSelectionModel().getSelectedItem());
        } else {
            btnProcess.setDisable(true);
            System.out.println("No value selected.");
        }
    }
}