package view.account;

import com.cathive.fx.guice.FXMLController;
import controller.database.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.databaseController.addChangeListener(account -> this.updateAccountList());
        this.updateAccountList();
    }

    @FXML
    public void handleItemClicked(final MouseEvent event) {
        final AccountListView accountListView = this.accountList.getSelectionModel().getSelectedItem();
        if (accountListView != null) {
            this.createOrEditAccount(accountListView.account);
        }
    }

    @FXML
    public void addAccount(final ActionEvent actionEvent) {
        this.createOrEditAccount(new Account());
    }

    private void createOrEditAccount(final Account account) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
        final AccountEdit controller = new AccountEdit(this.databaseController);
        controller.setAccount(account);
        fxmlLoader.setController(controller);
        try {
            final GridPane accountEditForm = fxmlLoader.load();
            this.mainPanel.getChildren().setAll(accountEditForm);
        } catch (IOException e) {
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
