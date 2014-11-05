package view;

import controller.DatabaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import view.account.AccountView;
import view.transaction.TransactionView;

import java.io.IOException;

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
            this.accountsTab.setContent(this.getAccountView());
            this.transactionTab.setContent(this.getTransactionView());
        }
    }

    private Parent getAccountView() {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("account/AccountView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final AccountView accountView = loader.getController();
        accountView.setDatabaseController(MainWindow.databaseController);
        return root;
    }

    private Parent getTransactionView() {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("transaction/TransactionView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final TransactionView transactionView = loader.getController();
        transactionView.setDatabaseController(MainWindow.databaseController);
        return root;
    }
}
