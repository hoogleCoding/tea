package view.transaction;

import viewmodel.transaction.TransactionEditViewModel;
import com.cathive.fx.guice.FXMLController;
import controller.layout.OverlayProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import view.account.AccountListView;

import javax.inject.Inject;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 11/12/14.
 */
@FXMLController
public class TransactionEditView {

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
    private OverlayProvider overlayProvider;
    private TransactionEditViewModel viewModel;

    public void setViewModel(final TransactionEditViewModel viewModel) {
        this.viewModel = viewModel;

        this.transactionName.textProperty().bindBidirectional(this.viewModel.getNameProperty());
        this.nameErrorTooltip.textProperty().bind(this.viewModel.getNameErrors());
        this.nameError.visibleProperty().bind(this.viewModel.getNameErrors().isNotEmpty());

        this.source.itemsProperty().bind(this.viewModel.getAccounts());
        this.source.valueProperty().bindBidirectional(this.viewModel.getSourceProperty());
        this.sourceErrorTooltip.textProperty().bind(this.viewModel.getSourceErrors());
        this.sourceError.visibleProperty().bind(this.viewModel.getSourceErrors().isNotEmpty());

        this.sink.itemsProperty().bind(this.viewModel.getAccounts());
        this.sink.valueProperty().bindBidirectional(this.viewModel.getSinkProperty());
        this.sinkErrorTooltip.textProperty().bind(this.viewModel.getSinkErrors());
        this.sinkError.visibleProperty().bind(this.viewModel.getSinkErrors().isNotEmpty());

        this.amountCurrency.itemsProperty().bind(this.viewModel.getCurrencies());
        this.amountCurrency.valueProperty().bindBidirectional(this.viewModel.getCurrencyProperty());
        this.amountValue.textProperty().bindBidirectional(this.viewModel.getAmountProperty());
        this.amountErrorTooltip.textProperty().bind(this.viewModel.getAmountErrors());
        this.amountError.visibleProperty().bind(this.viewModel.getAmountErrors().isNotEmpty());

        this.date.valueProperty().bindBidirectional(this.viewModel.getDateProperty());
        this.dateErrorTooltip.textProperty().bind(this.viewModel.getDateErrors());
        this.dateError.visibleProperty().bind(this.viewModel.getDateErrors().isNotEmpty());
    }

    @FXML
    public void save() {
        if (this.viewModel.save()) {
            this.overlayProvider.dispose();
        }
    }

    public void cancel(final ActionEvent actionEvent) {
        this.overlayProvider.dispose();
    }
}
