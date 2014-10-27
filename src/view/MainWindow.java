package view;

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

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
public class MainWindow implements Initializable {

    private ObservableList<AccountView> accountViews;
    @FXML
    private ListView<AccountView> accountList;
    @FXML
    private Pane mainPanel;

    public MainWindow() {
        List<AccountView> accounts = new LinkedList<>();
        accounts.add(new AccountView(new Account("TestAccount1")));
        accounts.add(new AccountView(new Account("TestAccount2")));
        this.accountViews = FXCollections.observableList(accounts);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
