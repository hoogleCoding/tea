package view.account;

import com.cathive.fx.guice.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import viewmodel.account.AccountListingViewModel;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
@FXMLController
public class AccountListingView implements Initializable {

    @FXML
    private ListView<AccountListView> accountList;
    @FXML
    private Pane mainPanel;
    @Inject
    private AccountListingViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.accountList.itemsProperty().bind(this.viewModel.getAccountListProperty());
    }

    @FXML
    public void handleItemClicked(final MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            final AccountListView accountListView = this.accountList.getSelectionModel().getSelectedItem();
            if (accountListView != null) {
                this.mainPanel.getChildren().setAll(this.viewModel.getAccountDashboard(accountListView.account));
                if (event.getClickCount() == 2) {
                    this.viewModel.editAccount(accountListView.account);
                }
            }
        }
    }

    @FXML
    public void addAccount(final ActionEvent actionEvent) {
        this.viewModel.addAccount();
    }
}
