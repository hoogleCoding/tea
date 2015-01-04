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
import org.javamoney.moneta.FastMoney;

import javax.inject.Inject;
import javax.inject.Named;
import javax.money.MonetaryAmount;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
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
    @FXML
    public Label currency;
    @FXML
    public Label balance;
    @Inject
    DatabaseController controller;
    @Inject
    @Named("i18n-resources")
    private ResourceBundle resources;
    private Account account;

    public void setAccount(final Account account) {
        this.account = account;
        this.header.setText(String.format(this.resources.getString("AccountEdit.Account_header"), account.getName()));
        if (account.getDescription().isPresent()) {
            this.description.setVisible(true);
            this.description.setText(account.getDescription().get());
        } else {
            this.description.setVisible(false);
            this.description.setText("");
        }
        this.currency.setText(this.account.getCurrency().getCurrencyCode());
        this.eventList.setItems(this.getRecentEvents());
        this.balance.setText(this.getBalance().toString());
    }

    private MonetaryAmount getBalance() {
        final MonetaryAmount in = this.getTotal(
                this.controller.getTransactions(this.account),
                transaction -> transaction.getSink().get().equals(this.account));
        final MonetaryAmount out = this.getTotal(
                this.controller.getTransactions(this.account),
                transaction -> transaction.getSource().get().equals(this.account));
        return in.subtract(out);
    }

    private MonetaryAmount getTotal(
            final Collection<Transaction> transactions,
            final Predicate<Transaction> filter) {
        return transactions
                .stream()
                .filter(filter)
                .map(Transaction::getAmount)
                .map(Optional::get)
                .reduce(FastMoney.of(0, this.account.getCurrency()), MonetaryAmount::add);
    }

    private ObservableList<Transaction> getRecentEvents() {
        final List<Transaction> transactions = this.controller
                .getTransactions(this.account)
                .stream()
                .sorted((a, b) -> a.getDate().get().compareTo(b.getDate().get()))
                .collect(Collectors.toList());
        return FXCollections.observableList(transactions);
    }
}
