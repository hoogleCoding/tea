package view;

import controller.AccountController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public class ViewInitializer extends Application {

    private static AccountController accountController;

    @Inject
    public void setAccountController(final AccountController controller) {
        if (ViewInitializer.accountController == null) {
            ViewInitializer.accountController = controller;
        }
    }

    public void run(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        MainWindow controller = loader.getController();
        controller.setAccountController(ViewInitializer.accountController);
        primaryStage.setTitle("Fugger 0.1");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}
