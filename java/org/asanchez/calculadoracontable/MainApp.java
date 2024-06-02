package org.asanchez.calculadoracontable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application  {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("calculadoraView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 820);
        stage.setTitle("Calculadora de Nomina 2024");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch();}
}