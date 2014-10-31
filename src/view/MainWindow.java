package view;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/31/14.
 */
public class MainWindow {
    private static AccountController accountController;
    @FXML
    public Tab accountsTab;

    public void setAccountController(final AccountController controller) {
        if (MainWindow.accountController == null) {
            MainWindow.accountController = controller;
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountView.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AccountView accountView = loader.getController();
            accountView.setAccountController(MainWindow.accountController);
            this.accountsTab.setContent(root);
        }
    }
}
