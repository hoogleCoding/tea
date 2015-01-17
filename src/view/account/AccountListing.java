package view.account;

import ViewModel.account.AccountEditViewModel;
import ViewModel.account.AccountOverviewViewModel;
import com.cathive.fx.guice.FXMLController;
import com.cathive.fx.guice.GuiceFXMLLoader;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.Account;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
@FXMLController
public class AccountListing implements Initializable {

    @FXML
    private ListView<AccountListView> accountList;
    @FXML
    private Pane mainPanel;
    @Inject
    private DatabaseController databaseController;
    @Inject
    private GuiceFXMLLoader fxmlLoader;
    @Inject
    private OverlayProvider overlayProvider;
    @Inject
    private AccountEditViewModel editViewModel;
    @Inject
    private AccountOverviewViewModel overviewViewModel;
    @Inject
    @Named("i18n-resources")
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.databaseController.addAccountChangeListener(account -> this.updateAccountList());
        this.updateAccountList();
    }

    @FXML
    public void handleItemClicked(final MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            final AccountListView accountListView = this.accountList.getSelectionModel().getSelectedItem();
            if (accountListView != null) {
                this.showAccountOverview(accountListView.account);
                if (event.getClickCount() == 2) {
                    this.showAccountEdit(accountListView.account);
                }
            }
        }
    }

    @FXML
    public void addAccount(final ActionEvent actionEvent) {
        this.showAccountEdit(new Account());
    }

    private void showAccountOverview(final Account account) {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(getClass().getResource("AccountOverviewView.fxml"), resources);
            this.overviewViewModel.setAccount(account);
            final AccountOverviewView view = result.getController();
            view.setViewModel(this.overviewViewModel);
            this.mainPanel.getChildren().setAll(result.<Node>getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAccountEdit(final Account account) {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(getClass().getResource("AccountEditView.fxml"), resources);
            final AccountEditView view = result.getController();
            this.editViewModel.setAccount(account);
            view.setViewModel(this.editViewModel);
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }

    private void updateAccountList() {
        final List<AccountListView> accounts = this.databaseController
                .getAccounts()
                .stream()
                .map(AccountListView::new)
                .sorted((one, other) -> one.getName().compareTo(other.getName()))
                .collect(Collectors.toList());
        ObservableList<AccountListView> accountListViews = FXCollections.observableList(accounts);
        this.accountList.setItems(accountListViews);
    }

}
