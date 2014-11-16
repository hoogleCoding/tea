package view.transaction;

import controller.DatabaseController;
import controller.DatabaseControllerReceiver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import view.account.AccountListView;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 11/12/14.
 */
public class TransactionEdit implements DatabaseControllerReceiver {

    @FXML
    public ComboBox<String> amountCurrency;
    @FXML
    public TextField transactionName;
    @FXML
    public TextField amountValue;
    @FXML
    public Label nameError;
    @FXML
    public Label amountError;
    @FXML
    public Label sourceError;
    @FXML
    public Label sinkError;
    @FXML
    public Tooltip nameErrorTooltip;
    @FXML
    public Tooltip amountErrorTooltip;
    @FXML
    public Tooltip sourceErrorTooltip;
    @FXML
    public Tooltip sinkErrorTooltip;
    @FXML
    ComboBox<AccountListView> source;
    @FXML
    ComboBox<AccountListView> sink;
    private DatabaseController controller;

    @Override
    public void setDatabaseController(final DatabaseController controller) {
        if (controller != null) {
            this.controller = controller;
            this.populate();
        }
    }

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
        this.validate();
    }

    private boolean validate() {
        //TODO: Write tests for the validation.
        this.validateTransactionName();
        this.validateAmount();
        this.validateSource();
        this.validateSink();
        return false;
    }

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
     * Validates the amount information and sets error messages.
     *
     * @return true if the information is valid; false otherwise.
     */
    private boolean validateAmount() {
        final List<String> messages = new LinkedList<>();
        if (this.amountValue.getText().isEmpty()) {
            messages.add("The transaction needs an amount.");
        } else {
            final String amount = this.amountValue.getText();
            try {
                new BigDecimal(amount);
            } catch (NumberFormatException exception) {
                messages.add(String.format("The value %s is not a decimal", amount));
            }
        }
        if (this.amountCurrency.getSelectionModel().getSelectedItem() == null) {
            messages.add("The transaction needs a currency.");
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
}
