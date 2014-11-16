package view;

import controller.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/31/14.
 */
public class MainWindow {
    private static DatabaseController databaseController;
    @FXML
    public Tab accountsTab;
    @FXML
    public Tab transactionTab;

    public void setAccountController(final DatabaseController controller) {
        if (MainWindow.databaseController == null) {
            MainWindow.databaseController = controller;
            this.accountsTab.setContent(
                    ViewLoader.getInitializedView(
                            "account/AccountView.fxml",
                            MainWindow.databaseController));
            this.transactionTab.setContent(
                    ViewLoader.getInitializedView(
                            "transaction/TransactionView.fxml",
                            MainWindow.databaseController));
        }
    }
}
