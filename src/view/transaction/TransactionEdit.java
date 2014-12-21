package view.transaction;

import com.cathive.fx.guice.FXMLController;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Transaction;
import org.javamoney.moneta.RoundedMoney;
import view.account.AccountListView;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 11/12/14.
 */
@FXMLController
public class TransactionEdit implements Initializable {

    //<editor-fold desc="FXML fields">
    @FXML
    private ComboBox<String> amountCurrency;
    @FXML
    private TextField transactionName;
    @FXML
    private DatePicker date;
    @FXML
    private TextField amountValue;
    @FXML
    private ComboBox<AccountListView> source;
    @FXML
    private ComboBox<AccountListView> sink;
    @FXML
    private Label nameError;
    @FXML
    private Label amountError;
    @FXML
    private Label sourceError;
    @FXML
    private Label sinkError;
    @FXML
    private Label dateError;
    @FXML
    private Tooltip nameErrorTooltip;
    @FXML
    private Tooltip amountErrorTooltip;
    @FXML
    private Tooltip sourceErrorTooltip;
    @FXML
    private Tooltip sinkErrorTooltip;
    @FXML
    private Tooltip dateErrorTooltip;
    //</editor-fold>
    @Inject
    private DatabaseController controller;
    @Inject
    private OverlayProvider overlayProvider;
    private Transaction transaction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.populate();
    }

    /**
     * Takes the values from a {@link model.Transaction} object and uses them to fill in the field of the form.
     *
     * @param transaction The transaction to take values from.
     */
    public void setTransaction(final Transaction transaction) {
        this.transaction = transaction;
        this.transaction.getName().ifPresent(this.transactionName::setText);
        this.transaction.getDate().ifPresent(timestamp -> this.date.setValue(LocalDate.ofEpochDay(timestamp)));
        this.transaction.getAmount().ifPresent(amount -> {
            this.amountCurrency.setValue(amount.getCurrency().getCurrencyCode());
            this.amountValue.setText(amount.getNumber().toString());
        });
        this.transaction.getSource().ifPresent(item ->
                        this.source.getItems()
                                .stream()
                                .filter(source -> source.account.equals(item))
                                .findAny()
                                .ifPresent(this.source.getSelectionModel()::select)
        );
        this.transaction.getSink().ifPresent(item ->
                        this.sink.getItems()
                                .stream()
                                .filter(sink -> sink.account.equals(item))
                                .findAny()
                                .ifPresent(this.sink.getSelectionModel()::select)
        );
    }

    /**
     * Populates the comboboxes with the appropriate values.
     */
    public void populate() {
        final List<AccountListView> accounts = this.controller
                .getAccounts()
                .stream()
                .map(AccountListView::new)
                .sorted((one, other) -> one.getName().compareTo(other.getName()))
                .collect(Collectors.toList());
        final ObservableList<AccountListView> sourceList = FXCollections.observableList(new LinkedList<>());
        sourceList.addAll(accounts);
        this.source.setItems(sourceList);
        this.sink.setItems(sourceList);
        this.amountCurrency.setItems(FXCollections.observableList(this.getCurrencies()));
        this.amountCurrency.getSelectionModel().select(0);
    }

    @FXML
    public void saveTransaction() {
        if (this.validate()) {
            this.transaction.setName(this.transactionName.getText());
            this.transaction.setSource(this.source.getSelectionModel().getSelectedItem().account);
            this.transaction.setSink(this.sink.getSelectionModel().getSelectedItem().account);
            final Number amount = new BigDecimal(this.amountValue.getText());
            this.transaction.setAmount(RoundedMoney.of(amount, this.amountCurrency.getValue()));
            this.transaction.setDate(this.date.getValue().toEpochDay());
            this.controller.save(this.transaction);
            this.overlayProvider.dispose();
        }
    }

    private boolean validate() {
        //TODO: Write tests for the validation.
        return this.validateTransactionName() &&
                this.validateDate() &&
                this.validateAmount() &&
                this.validateSource() &&
                this.validateSink();
    }

    /**
     * Checks whether the transaction name field has a value.
     *
     * @return True if the name is set, false otherwise.
     */
    private boolean validateTransactionName() {
        final List<String> messages = new LinkedList<>();
        if (this.transactionName.getText().isEmpty()) {
            messages.add("The transaction needs a name");
        }
        if (messages.isEmpty()) {
            this.nameError.visibleProperty().setValue(false);
            this.nameErrorTooltip.setText("");
            this.transactionName.getStyleClass().remove("error");
        } else {
            this.nameError.visibleProperty().setValue(true);
            this.transactionName.getStyleClass().add("error");
            this.nameErrorTooltip.setText(
                    messages
                            .stream()
                            .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", ""));
        }
        return messages.isEmpty();
    }

    /**
     * Checks whether the transaction has a date set.
     *
     * @return True if the date is set, false otherwise.
     */
    private boolean validateDate() {
        final List<String> messages = new LinkedList<>();
        if (this.date.getValue() == null) {
            messages.add("The transaction needs a date");
        }
        if (messages.isEmpty()) {
            this.dateError.visibleProperty().setValue(false);
            this.nameErrorTooltip.setText("");
            this.date.getStyleClass().remove("error");
        } else {
            this.dateError.visibleProperty().setValue(true);
            this.date.getStyleClass().add("error");
            this.dateErrorTooltip.setText(
                    messages
                            .stream()
                            .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", ""));
        }
        return messages.isEmpty();
    }

    /**
     * Validates the amount information and sets error messages.
     *
     * @return true if the information is valid; false otherwise.
     */
    private boolean validateAmount() {
        final List<String> messages = new LinkedList<>();
        if (this.amountValue.getText().isEmpty()) {
            messages.add("The transaction needs an amount");
        } else {
            final String amount = this.amountValue.getText();
            try {
                new BigDecimal(amount);
            } catch (NumberFormatException exception) {
                messages.add(String.format("The value %s is not a decimal", amount));
            }
        }
        if (this.amountCurrency.getSelectionModel().getSelectedItem() == null) {
            messages.add("The transaction needs a currency");
        }
        if (messages.isEmpty()) {
            this.amountValue.getStyleClass().remove("error");
            this.amountCurrency.getStyleClass().remove("error");
            this.amountError.visibleProperty().setValue(false);
        } else {
            this.amountValue.getStyleClass().add("error");
            this.amountCurrency.getStyleClass().add("error");
            this.amountError.visibleProperty().setValue(true);
            this.amountErrorTooltip.setText(
                    messages
                            .stream()
                            .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", ""));
        }
        return messages.isEmpty();
    }

    /**
     * Validates the source field and sets error display and messages accordingly.
     *
     * @return true if the field is valid; false otherwise;
     */
    private boolean validateSource() {
        final List<String> messages = new LinkedList<>();
        if (this.source.getSelectionModel().getSelectedItem() == null) {
            messages.add("The transaction needs a source");
        } else {
            final AccountListView selectedSource = this.source.getSelectionModel().getSelectedItem();
            if (this.sink.getSelectionModel().getSelectedItem() != null) {
                final AccountListView selectedSink = this.sink.getSelectionModel().getSelectedItem();
                if (selectedSource.equals(selectedSink)) {
                    messages.add("The sink of the transaction must be different from the source");
                }
            }
        }

        if (messages.isEmpty()) {
            this.source.getStyleClass().remove("error");
            this.sourceError.visibleProperty().setValue(false);
        } else {
            this.source.getStyleClass().add("error");
            this.sourceError.visibleProperty().setValue(true);
            this.sourceErrorTooltip.setText(
                    messages
                            .stream()
                            .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", ""));
        }
        return messages.isEmpty();
    }

    /**
     * Validates the source field and sets error display and messages accordingly.
     *
     * @return true if the field is valid; false otherwise;
     */
    private boolean validateSink() {
        final List<String> messages = new LinkedList<>();
        if (this.sink.getSelectionModel().getSelectedItem() == null) {
            messages.add("The transaction needs a sink");
        } else {
            final AccountListView selectedSink = this.sink.getSelectionModel().getSelectedItem();
            if (this.source.getSelectionModel().getSelectedItem() != null) {
                final AccountListView selectedSource = this.source.getSelectionModel().getSelectedItem();
                if (selectedSink.equals(selectedSource)) {
                    messages.add("The source of the transaction must be different from the sink");
                }
            }
        }

        if (messages.isEmpty()) {
            this.sink.getStyleClass().remove("error");
            this.sinkError.visibleProperty().setValue(false);
        } else {
            this.sink.getStyleClass().add("error");
            this.sinkError.visibleProperty().setValue(true);
            this.sinkErrorTooltip.setText(
                    messages
                            .stream()
                            .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", ""));
        }
        return messages.isEmpty();
    }

    private List<String> getCurrencies() {
        List<String> currencies = new LinkedList<>();
        currencies.add("CHF");
        currencies.add("EUR");
        currencies.add("USD");
        return currencies;
    }

    public void cancel(final ActionEvent actionEvent) {
        this.overlayProvider.dispose();
    }
}
