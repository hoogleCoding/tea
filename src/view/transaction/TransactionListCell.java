package view.transaction;

import javafx.scene.control.ListCell;
import model.Transaction;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 11/1/14.
 */
public class TransactionListCell extends ListCell<Transaction> {
    private Transaction transaction;

    @Override
    public String toString() {
        return "TransactionListCell{" +
                "transaction=" + transaction.toString() +
                '}';
    }

    @Override
    protected void updateItem(final Transaction item, boolean empty) {
        super.updateItem(item, empty);
        this.transaction = item;
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            this.setText(
                    String.format(
                            "%s from %s to %s",
                            item.getAmount().toString(),
                            item.getSource().getName(),
                            item.getSink().getName()));
        }
    }
}
