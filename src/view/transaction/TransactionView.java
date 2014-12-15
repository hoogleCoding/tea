package view.transaction;

import com.cathive.fx.guice.FXMLController;
import com.cathive.fx.guice.GuiceFXMLLoader;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Transaction;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/31/14.
 */
@FXMLController
public class TransactionView implements Initializable {

    @FXML
    public ListView<Transaction> transactionList;
    @FXML
    public Pane editPane;
    @Inject
    private DatabaseController databaseController;
    @Inject
    private GuiceFXMLLoader fxmlLoader;
    @Inject
    private OverlayProvider overlayProvider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Transaction> listContent = FXCollections.observableList(new LinkedList<>());
        List<Transaction> res = this.databaseController
                .getTransactions()
                .stream()
                .collect(Collectors.toList());
        listContent.addAll(res);
        this.transactionList.setItems(listContent);
        try {
            final GridPane node = this.fxmlLoader.load(getClass().getResource("TransactionEdit.fxml")).getRoot();
            this.editPane.getChildren().add(node);
            this.editPane.setPrefHeight(node.getPrefHeight());
        } catch (IOException e) {
            //TODO log
            e.printStackTrace();
        }
        this.transactionList.setCellFactory(callback -> new TransactionListCell());
    }

    @FXML
    public void handleClick(Event event) {
        //pass
    }

    @FXML
    public void addTransaction(final ActionEvent actionEvent) {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(getClass().getResource("TransactionEdit.fxml"));
            TransactionEdit controller = result.getController();
            controller.setTransaction(new Transaction());
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }
}
