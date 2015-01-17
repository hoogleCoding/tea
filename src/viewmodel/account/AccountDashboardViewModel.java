package viewmodel.account;

import controller.MoneyHelper;
import controller.database.DatabaseController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Account;
import model.Transaction;
import org.javamoney.moneta.FastMoney;
import viewmodel.transaction.TransactionListItemViewModel;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/14/15.
 */
public class AccountDashboardViewModel {


    private final DatabaseController controller;
    private StringProperty nameProperty;
    private StringProperty descriptionProperty;
    private StringProperty currencyProperty;
    private StringProperty balanceProperty;
    private ListProperty<TransactionListItemViewModel> eventListProperty;

    @Inject
    public AccountDashboardViewModel(final DatabaseController controller) {
        this.controller = controller;
        this.controller.addAccountChangeListener(this::setAccount);
    }

    public void setAccount(final Account account) {
        account.getName().ifPresent(this.getNameProperty()::setValue);
        account.getDescription().ifPresent(this.getDescriptionProperty()::setValue);
        account.getCurrency().ifPresent(currency -> this.getCurrencyProperty().setValue(currency.getCurrencyCode()));
        this.getBalanceProperty().setValue(this.getBalance(account).getNumber().toString());
        this.getEventListProperty().setValue(this.getRecentEvents(account));
    }

    public StringProperty getNameProperty() {
        if (this.nameProperty == null) {
            this.nameProperty = new SimpleStringProperty();
        }
        return this.nameProperty;
    }

    public StringProperty getDescriptionProperty() {
        if (this.descriptionProperty == null) {
            this.descriptionProperty = new SimpleStringProperty();
        }
        return this.descriptionProperty;
    }

    public StringProperty getCurrencyProperty() {
        if (this.currencyProperty == null) {
            this.currencyProperty = new SimpleStringProperty();
        }
        return this.currencyProperty;
    }

    public StringProperty getBalanceProperty() {
        if (this.balanceProperty == null) {
            this.balanceProperty = new SimpleStringProperty();
        }
        return this.balanceProperty;
    }

    public ListProperty<TransactionListItemViewModel> getEventListProperty() {
        if (this.eventListProperty == null) {
            this.eventListProperty = new SimpleListProperty<>();
        }
        return this.eventListProperty;
    }

    private MonetaryAmount getBalance(final Account account) {
        final CurrencyUnit currency = account.getCurrency().orElse(MoneyHelper.DEFAULT_CURRENCY);
        final Collection<Transaction> transactions = this.controller.getTransactions(account);
        final MonetaryAmount in = this.getTotal(
                currency,
                transactions,
                transaction -> transaction.getSink().get().equals(account));
        final MonetaryAmount out = this.getTotal(
                currency,
                transactions,
                transaction -> transaction.getSource().get().equals(account));
        return in.subtract(out);
    }

    private MonetaryAmount getTotal(
            final CurrencyUnit currency,
            final Collection<Transaction> transactions,
            final Predicate<Transaction> filter) {
        return transactions
                .stream()
                .filter(filter)
                .map(Transaction::getAmount)
                .map(Optional::get)
                .reduce(FastMoney.of(0, currency), MonetaryAmount::add);
    }

    private ObservableList<TransactionListItemViewModel> getRecentEvents(final Account account) {
        final List<TransactionListItemViewModel> transactions = this.controller
                .getTransactions(account)
                .stream()
                .sorted((a, b) -> a.getDate().get().compareTo(b.getDate().get()))
                .map(TransactionListItemViewModel::new)
                .collect(Collectors.toList());
        return FXCollections.observableList(transactions);
    }
}
