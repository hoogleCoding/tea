package view.transaction;

import controller.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Transaction;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/31/14.
 */
public class TransactionView implements Initializable {

    @FXML
    public ListView<Transaction> transactionList;
    private DatabaseController databaseController;


    public void setDatabaseController(final DatabaseController controller) {
        this.databaseController = controller;
        ObservableList<Transaction> listContent = FXCollections.observableList(new LinkedList<>());
        List<Transaction> res = this.databaseController
                .getTransactions()
                .stream()
                .collect(Collectors.toList());
        listContent.addAll(res);
        this.transactionList.setItems(listContent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.transactionList.setCellFactory(callback -> new TransactionListCell());
    }
}
