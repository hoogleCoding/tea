package view.account;

import com.cathive.fx.guice.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Account;
import viewmodel.account.AccountDashboardViewModel;
import viewmodel.transaction.TransactionListItemViewModel;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 12/22/14.
 */
@FXMLController
public class AccountDashboardView implements Initializable {
    @FXML
    private Label header;
    @FXML
    private Label description;
    @FXML
    private ListView<TransactionListItemViewModel> eventList;
    @FXML
    private Label currency;
    @FXML
    private Label balance;
    @Inject
    private AccountDashboardViewModel viewModel;

    public void setAccount(final Account account) {
        if (this.viewModel != null) {
            this.viewModel.setAccount(account);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.header.textProperty().bind(viewModel.getNameProperty());
        this.description.textProperty().bind(viewModel.getDescriptionProperty());
        this.description.visibleProperty().bind(viewModel.getDescriptionProperty().isNotEmpty());
        this.currency.textProperty().bind(viewModel.getCurrencyProperty());
        this.eventList.itemsProperty().bind(viewModel.getEventListProperty());
        this.balance.textProperty().bind(viewModel.getBalanceProperty());
    }
}
