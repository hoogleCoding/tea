package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public class ViewInitializer extends Application {

    public void run(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("Fugger 0.1");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}
