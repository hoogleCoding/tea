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
public class MainWindow {

    @FXML
    private ListView<AccountView> accountList;
    @FXML
    private Pane mainPanel;
    private AccountController accountController;

    public void setAccountController(final AccountController controller) {
        this.accountController = controller;
        List<AccountView> accounts = this.accountController
                .getAccounts()
                .stream()
                .map(AccountView::new)
                .collect(Collectors.toList());
        ObservableList<AccountView> accountViews = FXCollections.observableList(accounts);
        this.accountList.setItems(accountViews);
    }

    @FXML
    public void handleItemClicked(final MouseEvent event) {
        final AccountView accountView = this.accountList.getSelectionModel().getSelectedItem();
        this.createOrEditAccount(accountView.account);
    }

    @FXML
    public void addAccount(final ActionEvent actionEvent) {
        this.createOrEditAccount(new Account());
    }

    private void createOrEditAccount(final Account account) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
        final AccountEdit controller = new AccountEdit();
        controller.setAccount(account);
        fxmlLoader.setController(controller);
        try {
            final GridPane accountEditForm = fxmlLoader.load();
            this.mainPanel.getChildren().setAll(accountEditForm);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
