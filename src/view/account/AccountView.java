package view.account;

import com.cathive.fx.guice.FXMLController;
import com.cathive.fx.guice.GuiceFXMLLoader;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.Account;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
@FXMLController
public class AccountView implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.databaseController.addAccountChangeListener(account -> this.updateAccountList());
        this.updateAccountList();
    }

    @FXML
    public void handleItemClicked(final MouseEvent event) {
        final AccountListView accountListView = this.accountList.getSelectionModel().getSelectedItem();
        if (accountListView != null) {
            this.showAccountEdit(accountListView.account);
        }
    }

    @FXML
    public void addAccount(final ActionEvent actionEvent) {
        this.showAccountEdit(new Account());
    }

    private void showAccountEdit(final Account account) {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(getClass().getResource("AccountEdit.fxml"));
            final AccountEdit controller = result.getController();
            controller.setAccount(account);
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
