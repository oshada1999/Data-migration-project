module com.cba.datamigration {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.cba.datamigration;
    exports com.cba.datamigration.controller;
    opens com.cba.datamigration.controller to javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml; // For .xlsx files
    requires java.sql;
    requires com.opencsv;
}