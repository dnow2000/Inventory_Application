package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * javadocs folder is called control and it is located under the Orme_Inventory_Project folder
 * @author Daphne Orme
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 860, 385));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
