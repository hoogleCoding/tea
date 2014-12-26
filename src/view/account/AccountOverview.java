package view.account;

import com.cathive.fx.guice.FXMLController;
import controller.database.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Account;
import model.Transaction;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 12/22/14.
 */
@FXMLController
public class AccountOverview {
    @FXML
    public Label header;
    @FXML
    public Label description;
    @FXML
    public ListView<Transaction> eventList;
    @Inject
    DatabaseController controller;
    private Account account;

    public void setAccount(final Account account) {
        this.account = account;
        this.header.setText(String.format("Overview of %s", account.getName()));
        if (account.getDescription().isPresent()) {
            this.description.setVisible(true);
            this.description.setText(account.getDescription().get());
        } else {
            this.description.setVisible(false);
            this.description.setText("");
        }
        this.eventList.setItems(this.getRecentEvents());
    }

    private ObservableList<Transaction> getRecentEvents() {
        final List<Transaction> transactions = this.controller
                .getTransactions(this.account)
                .stream()
                .sorted((a, b) -> a.getDate().get().compareTo(b.getDate().get()))
                .limit(5)
                .collect(Collectors.toList());
        return FXCollections.observableList(transactions);
    }
}
