package com.cba.datamigration.controller;

import com.cba.datamigration.dto.ChildDTO;
import com.cba.datamigration.dto.ProductDTO;
import com.cba.datamigration.mapper.ChildRowMapper;
import com.cba.datamigration.mapper.ProductRowMapper;
import com.cba.datamigration.model.ChildModel;
import com.cba.datamigration.model.ProductModel;
import com.cba.datamigration.util.FileDataReader;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductController {
    private FileDataReader fileDataReader;

    public ProductController() {
        this.fileDataReader = new FileDataReader();
    }

    public void processData(File file, Consumer<Boolean> onCompletion) {
        System.out.println("Processing file : " + file.getName());
        List<ProductDTO> products = new ArrayList<>();
        boolean success = false;
        try {
            if (file.getName().endsWith(".csv")) {
                products = fileDataReader.readCsv(file, new ProductRowMapper());
            } else {
                products = fileDataReader.readExcel(file, new ProductRowMapper());
            }

            if (products.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Valid Data", "No data found in the file.", null);
                onCompletion.accept(true);
                return;
            }

            ProductModel productModel = new ProductModel();
            //save product
            //productModel.saveProduct(products);
            //save warranty template
            //productModel.saveWarrantyTemplate(products);
            // save warranty agreement
            productModel.saveWarrantyAgreement(products);
            // save warranty agreement product
            productModel.saveWarrantyProduct(products);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Machine records inserted in batches.", null);
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
