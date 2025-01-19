package org.example.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KaraokeApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Carga el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Loggin.fxml"));
        Parent root = loader.load();
        stage.setTitle("Karaoke App");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}