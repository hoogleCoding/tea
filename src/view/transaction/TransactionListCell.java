package view.transaction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import model.Transaction;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 11/1/14.
 */
public class TransactionListCell extends ListCell<Transaction> {
    @FXML
    public Label transactionName;
    @FXML
    public Label amount;
    @FXML
    public Label source;
    @FXML
    public Label sink;
    @FXML
    public GridPane gridPane;

    public TransactionListCell(final ResourceBundle resources) {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionListCell.fxml"), resources);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(final Transaction item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            item.getName().ifPresent(this.transactionName::setText);
            item.getAmount().ifPresent(amount -> this.amount.setText(amount.toString()));
            item.getSource().ifPresent(value -> this.source.setText(value.getName()));
            item.getSink().ifPresent(value -> this.sink.setText(value.getName()));
            this.setGraphic(this.gridPane);
        }
    }
}
