package view.account;

import ViewModel.account.AccountOverviewViewModel;
import ViewModel.transaction.TransactionListItemViewModel;
import com.cathive.fx.guice.FXMLController;
import controller.database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.inject.Inject;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 12/22/14.
 */
@FXMLController
public class AccountOverviewView {
    @FXML
    public Label header;
    @FXML
    public Label description;
    @FXML
    public ListView<TransactionListItemViewModel> eventList;
    @FXML
    public Label currency;
    @FXML
    public Label balance;
    @Inject
    DatabaseController controller;

    void setViewModel(final AccountOverviewViewModel viewModel) {
        this.header.textProperty().bind(viewModel.getNameProperty());
        this.description.textProperty().bind(viewModel.getDescriptionProperty());
        this.description.visibleProperty().bind(viewModel.getDescriptionProperty().isNotEmpty());
        this.currency.textProperty().bind(viewModel.getCurrencyProperty());
        this.eventList.itemsProperty().bind(viewModel.getEventListProperty());
        this.balance.textProperty().bind(viewModel.getBalanceProperty());
    }
}
