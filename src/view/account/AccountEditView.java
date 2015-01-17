package view.account;

import com.cathive.fx.guice.FXMLController;
import controller.layout.OverlayProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Account;
import viewmodel.account.AccountEditViewModel;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/24/14.
 */
@FXMLController
public class AccountEditView implements Initializable {
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
    //</editor-fold>
    @Inject
    private AccountEditViewModel viewModel;

    public void setAccount(final Account account) {
        this.viewModel.setAccount(account);
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
        this.accountName.textProperty().bindBidirectional(viewModel.getNameProperty());
        this.description.textProperty().bindBidirectional(viewModel.getDescriptionProperty());
        this.currency.valueProperty().bindBidirectional(viewModel.getCurrencyProperty());
        this.currency.itemsProperty().bind(viewModel.getCurrencyList());
        this.accountNameErrorTooltip.textProperty().bind(viewModel.getNameErrors());
        this.accountNameError.visibleProperty().bind(viewModel.getNameErrors().isNotEmpty());
        this.currencyErrorTooltip.textProperty().bind(viewModel.getCurrencyErrors());
        this.currencyError.visibleProperty().bind(viewModel.getCurrencyErrors().isNotEmpty());
    }
}
