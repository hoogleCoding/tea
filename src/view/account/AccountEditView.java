package view.account;

import ViewModel.account.AccountEditViewModel;
import com.cathive.fx.guice.FXMLController;
import controller.layout.OverlayProvider;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static controller.MoneyHelper.getCurrencies;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/24/14.
 */
@FXMLController
public class AccountEditView implements Initializable {
    //</editor-fold>
    @Inject
    OverlayProvider overlayProvider;
    //<editor-fold desc="FXML fields">
    @FXML
    private Label accountNameError;
    @FXML
    private Tooltip accountNameErrorTooltip;
    @FXML
    private TextArea description;
    @FXML
    private ComboBox<String> currency;
    @FXML
    private Label currencyError;
    @FXML
    private Tooltip currencyErrorTooltip;
    @FXML
    private TextField accountName;
    private AccountEditViewModel viewModel;

    public void setViewModel(final AccountEditViewModel viewModel) {
        this.viewModel = viewModel;
        this.accountName.textProperty().bindBidirectional(viewModel.getNameProperty());
        this.description.textProperty().bindBidirectional(viewModel.getDescriptionProperty());
        this.currency.valueProperty().bindBidirectional(viewModel.getCurrencyProperty());
        this.accountNameErrorTooltip.textProperty().bind(viewModel.getNameErrors());
        this.accountNameError.visibleProperty().bind(viewModel.getNameErrors().isNotEmpty());
        this.currencyErrorTooltip.textProperty().bind(viewModel.getCurrencyErrors());
        this.currencyError.visibleProperty().bind(viewModel.getCurrencyErrors().isNotEmpty());
    }

    public void save() {
        if (this.viewModel.save()) {
            this.overlayProvider.dispose();
        }
    }

    public void cancel(final ActionEvent actionEvent) {
        this.overlayProvider.dispose();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.currency.setItems(FXCollections.observableList(getCurrencies()));
    }
}
