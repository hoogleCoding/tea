package viewmodel.transaction;

import com.cathive.fx.guice.GuiceFXMLLoader;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Transaction;
import view.transaction.TransactionEditView;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/18/15.
 */
public class TransactionListingViewModel {
    private final OverlayProvider overlayProvider;
    private final ResourceBundle resources;
    private ListProperty<Transaction> transactionListProperty;
    private DatabaseController databaseController;
    private GuiceFXMLLoader fxmlLoader;

    @Inject
    public TransactionListingViewModel(final DatabaseController databaseController,
                                       final GuiceFXMLLoader fxmlLoader,
                                       final OverlayProvider overlayProvider,
                                       @Named("i18n-resources") final ResourceBundle resourceBundle) {
        this.databaseController = databaseController;
        this.fxmlLoader = fxmlLoader;
        this.overlayProvider = overlayProvider;
        resources = resourceBundle;
        this.databaseController.addTransactionChangeListener(
                transaction -> this.getTransactionListProperty().setValue(this.updateTransactionList())
        );
    }

    public ListProperty<Transaction> getTransactionListProperty() {
        if (this.transactionListProperty == null) {
            this.transactionListProperty = new SimpleListProperty<>();
            this.transactionListProperty.setValue(this.updateTransactionList());
        }
        return this.transactionListProperty;
    }

    private ObservableList<Transaction> updateTransactionList() {
        final List<Transaction> res = this.databaseController
                .getTransactions()
                .stream()
                .collect(Collectors.toList());
        return FXCollections.observableList(res);
    }

    public void addTransaction() {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(
                    getClass().getResource("/view/transaction/TransactionEditView.fxml"),
                    this.resources);
            ((TransactionEditView) result.getController()).setTransaction(new Transaction());
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }

    public void editTransaction(final Transaction transaction) {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(
                    getClass().getResource("/view/transaction/TransactionEditView.fxml"),
                    this.resources);
            ((TransactionEditView) result.getController()).setTransaction(transaction);
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }
}
