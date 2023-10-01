package Wordle.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WordleApp extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Wordle");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Wordle.fxml"))));

        primaryStage.show();
    }
}
