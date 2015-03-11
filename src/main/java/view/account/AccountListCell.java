package view.account;

import javafx.scene.control.ListCell;
import model.Account;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 3/8/15.
 */
public class AccountListCell extends ListCell<Account> {

    @Override
    protected void updateItem(final Account item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            this.setText(null);
            this.setGraphic(null);
        } else {
            item.getName().ifPresent(this::setText);
        }
    }
}
