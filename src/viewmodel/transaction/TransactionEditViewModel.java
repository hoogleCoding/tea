package viewmodel.transaction;

import controller.MoneyHelper;
import controller.database.DatabaseController;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import model.Transaction;
import org.javamoney.moneta.FastMoney;
import view.account.AccountListView;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static viewmodel.ViewModelUtils.flattenMessages;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/5/15.
 */
public class TransactionEditViewModel {

    private final DatabaseController controller;
    private final ResourceBundle resources;
    private StringProperty nameProperty;
    private StringProperty nameErrors;
    private Property<AccountListView> sourceProperty;
    private StringProperty sourceErrors;
    private Property<AccountListView> sinkProperty;
    private StringProperty sinkErrors;
    private StringProperty amountProperty;
    private StringProperty currencyProperty;
    private StringProperty amountErrors;
    private Property<LocalDate> dateProperty;
    private StringProperty dateErrors;
    private ListProperty<AccountListView> accountList;
    private ListProperty<String> currencyList;

    private Transaction transaction;

    @Inject
    public TransactionEditViewModel(final DatabaseController controller,
                                    @Named("i18n-resources") final ResourceBundle resourceBundle) {
        this.controller = controller;
        this.resources = resourceBundle;
    }

    public void setTransaction(final Transaction transaction) {
        this.transaction = transaction;
    }

    public StringProperty getNameProperty() {
        if (this.nameProperty == null) {
            this.nameProperty = new SimpleStringProperty();
            this.transaction
                    .getName()
                    .ifPresent(this.nameProperty::setValue);
        }
        return this.nameProperty;
    }

    public Property<AccountListView> getSourceProperty() {
        if (this.sourceProperty == null) {
            this.sourceProperty = new SimpleObjectProperty<>();
            this.transaction
                    .getSource()
                    .ifPresent(source -> this.sourceProperty.setValue(AccountListView.of(source)));
        }
        return this.sourceProperty;
    }

    public Property<AccountListView> getSinkProperty() {
        if (this.sinkProperty == null) {
            this.sinkProperty = new SimpleObjectProperty<>();
            this.transaction
                    .getSink()
                    .ifPresent(sink -> this.sinkProperty.setValue(AccountListView.of(sink)));
        }
        return this.sinkProperty;
    }

    public StringProperty getCurrencyProperty() {
        if (this.currencyProperty == null) {
            this.currencyProperty = new SimpleStringProperty();
            this.transaction
                    .getAmount()
                    .ifPresent(amount -> this.currencyProperty.setValue(amount.getCurrency().getCurrencyCode()));
        }
        return currencyProperty;
    }

    public StringProperty getAmountProperty() {
        if (this.amountProperty == null) {
            this.amountProperty = new SimpleStringProperty();
            this.transaction
                    .getAmount()
                    .ifPresent(amount -> this.amountProperty.setValue(amount.getNumber().toString()));
        }
        return this.amountProperty;
    }

    public Property<LocalDate> getDateProperty() {
        if (this.dateProperty == null) {
            this.dateProperty = new SimpleObjectProperty<>();
            this.transaction
                    .getDate()
                    .ifPresent(date -> this.dateProperty.setValue(LocalDate.ofEpochDay(date)));
        }
        return dateProperty;
    }

    public StringProperty getNameErrors() {
        if (this.nameErrors == null) {
            this.nameErrors = new SimpleStringProperty();
        }
        return this.nameErrors;
    }

    public StringProperty getDateErrors() {
        if (this.dateErrors == null) {
            this.dateErrors = new SimpleStringProperty();
        }
        return this.dateErrors;
    }

    public StringProperty getAmountErrors() {
        if (this.amountErrors == null) {
            this.amountErrors = new SimpleStringProperty();
        }
        return this.amountErrors;
    }

    public StringProperty getSourceErrors() {
        if (this.sourceErrors == null) {
            this.sourceErrors = new SimpleStringProperty();
        }
        return this.sourceErrors;
    }

    public StringProperty getSinkErrors() {
        if (this.sinkErrors == null) {
            this.sinkErrors = new SimpleStringProperty();
        }
        return this.sinkErrors;
    }

    public ListProperty<AccountListView> getAccounts() {
        if (this.accountList == null) {
            this.accountList = new SimpleListProperty<>();
            final List<AccountListView> accounts = this.controller
                    .getAccounts()
                    .stream()
                    .map(AccountListView::new)
                    .sorted((one, other) -> one.getName().compareTo(other.getName()))
                    .collect(Collectors.toList());
            this.accountList.setValue(FXCollections.observableList(accounts));
        }
        return this.accountList;
    }

    public ListProperty<String> getCurrencies() {
        if (this.currencyList == null) {
            this.currencyList = new SimpleListProperty<>();
            this.currencyList.setValue(FXCollections.observableList(MoneyHelper.getCurrencies()));
        }
        return this.currencyList;
    }

    public boolean save() {
        return this.validate() && this.synchronizeModel() && this.controller.save(this.transaction) != null;
    }

    private boolean synchronizeModel() {
        boolean result = true;
        this.transaction.setName(this.getNameProperty().getValueSafe());
        this.transaction.setDate(this.getDateProperty().getValue().toEpochDay());
        final String currency = this.getCurrencyProperty().getValueSafe();
        final Number amount;
        try {
            amount = NumberFormat.getInstance().parse(this.getAmountProperty().getValueSafe());
            this.transaction.setAmount(FastMoney.of(amount, currency));
        } catch (ParseException e) {
            result = false;
        }
        this.transaction.setSource(this.getSourceProperty().getValue().account);
        this.transaction.setSink(this.getSinkProperty().getValue().account);
        return result;
    }

    private boolean validate() {
        final boolean validName = this.validateTransactionName();
        final boolean validDate = this.validateDate();
        final boolean validAmount = this.validateAmount();
        final boolean validSource = this.validateSource();
        final boolean validSink = this.validateSink();
        return validName && validDate && validAmount && validSource && validSink;
    }

    private boolean validateTransactionName() {
        final List<String> messages = new LinkedList<>();
        final String value = this.getNameProperty().getValueSafe();
        if (value.isEmpty()) {
            messages.add(this.resources.getString("TransactionEdit.Needs_name"));
        }
        this.getNameErrors().setValue(flattenMessages(messages));
        return messages.isEmpty();
    }

    /**
     * Checks whether the transaction has a date set.
     *
     * @return True if the date is set, false otherwise.
     */
    private boolean validateDate() {
        final List<String> messages = new LinkedList<>();
        final LocalDate value = this.getDateProperty().getValue();
        if (value == null) {
            messages.add(this.resources.getString("TransactionEdit.Needs_date"));
        }
        this.getDateErrors().setValue(flattenMessages(messages));
        return messages.isEmpty();
    }

    /**
     * Validates the amount information and sets error messages.
     *
     * @return true if the information is valid; false otherwise.
     */
    private boolean validateAmount() {
        final List<String> messages = new LinkedList<>();
        final String amount = this.getAmountProperty().getValueSafe();
        final String currency = this.getCurrencyProperty().getValue();
        if (amount.isEmpty()) {
            messages.add(this.resources.getString("TransactionEdit.Needs_amount"));
        } else {
            try {
                new BigDecimal(amount);
            } catch (NumberFormatException exception) {
                messages.add(String.format(this.resources.getString("TransactionEdit.Amount_not_a_number"), amount));
            }
        }
        if (currency == null) {
            messages.add(this.resources.getString("TransactionEdit.Needs_currency"));
        }
        this.getAmountErrors().setValue(flattenMessages(messages));
        return messages.isEmpty();
    }

    /**
     * Validates the source field and sets error display and messages accordingly.
     *
     * @return true if the field is valid; false otherwise;
     */
    private boolean validateSource() {
        final List<String> messages = new LinkedList<>();
        final AccountListView source = this.getSourceProperty().getValue();
        if (source == null) {
            messages.add(this.resources.getString("TransactionEdit.Needs_source"));
        } else {
            final AccountListView sink = this.getSinkProperty().getValue();
            if (source.equals(sink)) {
                messages.add(this.resources.getString("TransactionEdit.Sink_must_be_different"));
            }
            if (!source.account.getCurrency().equals(sink.account.getCurrency())) {
                messages.add(this.resources.getString("TransactionEdit.Sink_must_have_same_currency"));
            }
        }
        this.getSourceErrors().setValue(flattenMessages(messages));
        return messages.isEmpty();
    }

    private boolean validateSink() {
        final List<String> messages = new LinkedList<>();
        final AccountListView sink = this.getSinkProperty().getValue();
        if (sink == null) {
            messages.add(this.resources.getString("TransactionEdit.Needs_sink"));
        } else {
            final AccountListView source = this.getSourceProperty().getValue();
            if (sink.equals(source)) {
                messages.add(this.resources.getString("TransactionEdit.Source_must_be_different"));
            }
            if (!sink.account.getCurrency().equals(source.account.getCurrency())) {
                messages.add(this.resources.getString("TransactionEdit.Source_must_have_same_currency"));
            }
        }
        this.getSinkErrors().setValue(flattenMessages(messages));
        return messages.isEmpty();
    }
}
