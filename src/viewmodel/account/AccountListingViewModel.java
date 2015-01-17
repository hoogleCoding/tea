package viewmodel.account;

import com.cathive.fx.guice.GuiceFXMLLoader;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import model.Account;
import view.account.AccountDashboardView;
import view.account.AccountEditView;
import view.account.AccountListView;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/17/15.
 */
public class AccountListingViewModel {
    private final OverlayProvider overlayProvider;
    private final ResourceBundle resources;
    private final GuiceFXMLLoader fxmlLoader;
    private ListProperty<AccountListView> accountListProperty;
    private DatabaseController databaseController;

    @Inject
    public AccountListingViewModel(final GuiceFXMLLoader fxmlLoader,
                                   final DatabaseController databaseController,
                                   final OverlayProvider overlayProvider,
                                   @Named("i18n-resources") final ResourceBundle resourceBundle) {
        this.fxmlLoader = fxmlLoader;
        this.databaseController = databaseController;
        this.overlayProvider = overlayProvider;
        this.resources = resourceBundle;
        this.databaseController.addAccountChangeListener(
                account -> this.getAccountListProperty().setValue(this.updateAccountList())
        );
    }

    public ListProperty<AccountListView> getAccountListProperty() {
        if (this.accountListProperty == null) {
            this.accountListProperty = new SimpleListProperty<>(this.updateAccountList());
        }
        return this.accountListProperty;
    }

    private ObservableList<AccountListView> updateAccountList() {
        final List<AccountListView> accounts = this.databaseController
                .getAccounts()
                .stream()
                .map(AccountListView::new)
                .sorted((one, other) -> one.getName().compareTo(other.getName()))
                .collect(Collectors.toList());
        return FXCollections.observableList(accounts);
    }

    public void addAccount() {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(
                    getClass().getResource("../../view/account/AccountEditView.fxml"),
                    this.resources);
            ((AccountEditView) result.getController()).setAccount(new Account());
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }

    public Node getAccountDashboard(final Account account) {
        Node view = null;
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(
                    getClass().getResource("../../view/account/AccountDashboardView.fxml"),
                    resources);
            ((AccountDashboardView) result.getController()).setAccount(account);
            view = result.<Node>getRoot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void editAccount(final Account account) {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(
                    getClass().getResource("../../view/account/AccountEditView.fxml"),
                    resources);
            ((AccountEditView) result.getController()).setAccount(account);
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }
}
