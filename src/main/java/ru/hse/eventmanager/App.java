package ru.hse.eventmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

public class App extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        File loginFile = new File("C:/Users/hp/Documents/NetBeansProjects/EventManager/target/classes/fxml/login.fxml");
        Parent root = FXMLLoader.load(loginFile.toURI().toURL());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("EventManager");
        stage.show();
    }

    public static void setRoot(String fxml) throws Exception {
        File file = new File("C:/Users/hp/Documents/NetBeansProjects/EventManager/target/classes/fxml/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(file.toURI().toURL());
        stage.getScene().setRoot(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}