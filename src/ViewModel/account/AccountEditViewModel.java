package ViewModel.account;

import controller.database.DatabaseController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Account;

import javax.inject.Inject;
import javax.inject.Named;
import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;
import javax.money.MonetaryException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Provides the ViewModel for the AccountEditView
 * Created by Florian Hug <florian.hug@gmail.com> on 1/4/15.
 */
public class AccountEditViewModel {
    private final DatabaseController controller;
    private final ResourceBundle resources;

    private Account account;
    private StringProperty nameProperty;
    private StringProperty descriptionProperty;
    private StringProperty currencyProperty;
    private StringProperty nameErrors;
    private StringProperty currencyErrors;

    @Inject
    public AccountEditViewModel(final DatabaseController controller,
                                @Named("i18n-resources") final ResourceBundle resourceBundle) {
        this.controller = controller;
        this.resources = resourceBundle;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public StringProperty getNameProperty() {
        if (this.nameProperty == null) {
            this.nameProperty = new SimpleStringProperty(this.account.getName());
        }
        return nameProperty;
    }

    public StringProperty getDescriptionProperty() {
        if (this.descriptionProperty == null) {
            this.descriptionProperty = new SimpleStringProperty(this.account.getDescription().orElse(""));
        }
        return this.descriptionProperty;
    }

    public StringProperty getCurrencyProperty() {
        if (this.currencyProperty == null) {
            this.currencyProperty = new SimpleStringProperty(this.account.getCurrency().getCurrencyCode());
        }
        return this.currencyProperty;
    }

    public StringProperty getNameErrors() {
        if (this.nameErrors == null) {
            this.nameErrors = new SimpleStringProperty();
        }
        return this.nameErrors;
    }

    public StringProperty getCurrencyErrors() {
        if (this.currencyErrors == null) {
            this.currencyErrors = new SimpleStringProperty();
        }
        return this.currencyErrors;
    }

    public boolean save() {
        return this.validate() && this.synchronizeModel() && this.controller.save(this.account) != null;
    }

    public boolean validate() {
        final boolean nameValid = this.validateAccountName();
        final boolean currencyValid = this.validateCurrency();
        return nameValid && currencyValid;
    }

    private boolean synchronizeModel() {
        boolean result = true;
        this.account.setName(this.getNameProperty().getValueSafe());
        if (!this.getDescriptionProperty().getValueSafe().isEmpty()) {
            this.account.setDescription(this.getDescriptionProperty().getValueSafe());
        }
        try {
            final CurrencyUnit currency = MonetaryCurrencies.getCurrency(this.getCurrencyProperty().getValueSafe());
            this.account.setCurrency(currency);
        } catch (MonetaryException exception) {
            //TODO: log
            result = false;
        }
        return result;
    }

    private boolean validateCurrency() {
        final List<String> messages = new LinkedList<>();
        final String value = this.getCurrencyProperty().getValue();
        if (value.isEmpty()) {
            messages.add(this.resources.getString("AccountEdit.Account_needs_currency"));
        } else if (!this.account.getCurrency().getCurrencyCode().equals(this.getCurrencyProperty().getValueSafe())) {
            if (this.account.getId().isPresent() && this.controller.getTransactions(this.account).size() > 0) {
                messages.add(this.resources.getString("AccountEdit.Cannot_change_currency"));
            }
        }
        this.currencyErrors.set(flattenMessages(messages));
        return messages.isEmpty();
    }

    private boolean validateAccountName() {
        final List<String> messages = new LinkedList<>();
        final String value = this.getNameProperty().getValueSafe();
        if (value.isEmpty()) {
            messages.add(this.resources.getString("AccountEdit.Account_needs_name"));
        }
        //TODO: Check if account name exists in the database.
        this.nameErrors.set(flattenMessages(messages));
        return messages.isEmpty();
    }

    private String flattenMessages(final List<String> messages) {
        return messages
                .stream()
                .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", "");
    }
}
