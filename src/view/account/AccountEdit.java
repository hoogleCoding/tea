package view.account;

import com.cathive.fx.guice.FXMLController;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Account;

import javax.inject.Inject;
import javax.inject.Named;
import javax.money.MonetaryCurrencies;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static controller.MoneyHelper.getCurrencies;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/24/14.
 */
@FXMLController
public class AccountEdit implements Initializable {
    //<editor-fold desc="FXML fields">
    @FXML
    public Label accountNameError;
    @FXML
    public Tooltip accountNameErrorTooltip;
    @FXML
    public TextArea description;
    @FXML
    public ComboBox<String> currency;
    @FXML
    public Label currencyError;
    @FXML
    public Tooltip currencyErrorTooltip;
    @FXML
    TextField accountName;
    //</editor-fold>
    @Inject
    OverlayProvider overlayProvider;
    @Inject
    DatabaseController controller;
    @Inject
    @Named("i18n-resources")
    private ResourceBundle resources;
    private Account account = null;

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
        if (isNotEmpty(this.account.getName())) {
            this.accountName.setText(this.account.getName());
        }
        this.account.getDescription().ifPresent(this.description::setText);
        if (this.account.getCurrency() != null) {
            this.currency.setValue(account.getCurrency().getCurrencyCode());
        } else {
            this.currency.getSelectionModel().select(0);
        }
    }

    public void save() {
        if (this.account != null && this.validate()) {
            this.account.setName(this.accountName.getText());
            this.account.setDescription(this.description.getText());
            this.account.setCurrency(MonetaryCurrencies.getCurrency(this.currency.getValue()));
            this.controller.save(account);
            this.overlayProvider.dispose();
        }
    }

    public void cancel(final ActionEvent actionEvent) {
        this.overlayProvider.dispose();
    }

    boolean validate() {
        final boolean nameValid = this.validateAccountName();
        final boolean currencyValid = this.validateCurrency();
        return nameValid && currencyValid;
    }

    boolean validateAccountName() {
        final List<String> messages = new LinkedList<>();
        if (this.accountName.getText().isEmpty()) {
            messages.add(this.resources.getString("AccountEdit.Account_needs_name"));
        }
        if (messages.isEmpty()) {
            this.accountNameError.visibleProperty().setValue(false);
            this.accountNameErrorTooltip.setText("");
            this.accountName.getStyleClass().remove("error");
        } else {
            this.accountNameError.visibleProperty().setValue(true);
            this.accountName.getStyleClass().add("error");
            this.accountNameErrorTooltip.setText(
                    messages
                            .stream()
                            .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", ""));
        }
        return messages.isEmpty();
    }

    boolean validateCurrency() {
        final List<String> messages = new LinkedList<>();
        if (this.currency.getValue() == null || this.currency.getValue().isEmpty()) {
            messages.add(this.resources.getString("AccountEdit.Account_needs_currency"));
        } else {
            if (this.account.getId().isPresent() && this.controller.getTransactions(this.account).size() > 0) {
                messages.add(this.resources.getString("AccountEdit.Cannot_change_currency"));
            }
        }
        if (messages.isEmpty()) {
            this.currencyError.visibleProperty().setValue(false);
            this.currencyErrorTooltip.setText("");
            this.currency.getStyleClass().remove("error");

        } else {
            this.currencyError.visibleProperty().setValue(true);
            this.currency.getStyleClass().add("error");
            this.currencyErrorTooltip.setText(
                    messages
                            .stream()
                            .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", ""));

        }
        return messages.isEmpty();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.currency.setItems(FXCollections.observableList(getCurrencies()));
    }
}
