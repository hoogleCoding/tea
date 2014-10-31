package view;

import controller.AccountController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Account;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
public class AccountView {

    @FXML
    private ListView<AccountListView> accountList;
    @FXML
    private Pane mainPanel;
    private AccountController accountController;

    public void setAccountController(final AccountController controller) {
        this.accountController = controller;
        this.accountController.addChangeListener(account -> this.updateAccountList());
        this.updateAccountList();
    }

    @FXML
    public void handleItemClicked(final MouseEvent event) {
        final AccountListView accountListView = this.accountList.getSelectionModel().getSelectedItem();
        this.createOrEditAccount(accountListView.account);
    }

    @FXML
    public void addAccount(final ActionEvent actionEvent) {
        this.createOrEditAccount(new Account());
    }

    private void createOrEditAccount(final Account account) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
        final AccountEdit controller = new AccountEdit(this.accountController);
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
        final List<AccountListView> accounts = this.accountController
                .getAccounts()
                .stream()
                .map(AccountListView::new)
                .sorted((one, other) -> one.getName().compareTo(other.getName()))
                .collect(Collectors.toList());
        ObservableList<AccountListView> accountListViews = FXCollections.observableList(accounts);
        this.accountList.setItems(accountListViews);
    }
}
