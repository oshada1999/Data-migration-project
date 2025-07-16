package com.cba.datamigration;

import com.cba.datamigration.util.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/com/cba/datamigration/view/MainView.fxml")); // Corrected path
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("CBA Data Migration Tool");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {

        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}