package view.transaction;

import ViewModel.transaction.TransactionEditViewModel;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Transaction;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/31/14.
 */
@FXMLController
public class TransactionListing implements Initializable {

    @FXML
    public ListView<Transaction> transactionList;
    @Inject
    private DatabaseController databaseController;
    @Inject
    private GuiceFXMLLoader fxmlLoader;
    @Inject
    private OverlayProvider overlayProvider;
    @Inject
    private TransactionEditViewModel transactionEditViewModel;
    @Inject
    @Named("i18n-resources")
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.databaseController.addTransactionChangeListener(transaction -> this.updateTransactionList());
        this.updateTransactionList();
    }

    private void updateTransactionList() {
        final List<Transaction> res = this.databaseController
                .getTransactions()
                .stream()
                .collect(Collectors.toList());
        final ObservableList<Transaction> listContent = FXCollections.observableList(res);
        this.transactionList.setItems(listContent);
        this.transactionList.setCellFactory(callback -> new TransactionListCell(this.resources));
    }

    @FXML
    public void handleClick(final MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            final Transaction transaction = this.transactionList.getSelectionModel().getSelectedItem();
            this.showTransactionEdit(transaction);
        }
    }

    @FXML
    public void addTransaction(final ActionEvent actionEvent) {
        this.showTransactionEdit(new Transaction());
    }

    private void showTransactionEdit(final Transaction transaction) {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(getClass().getResource("TransactionEditView.fxml"), this.resources);
            final TransactionEditView controller = result.getController();
            this.transactionEditViewModel.setTransaction(transaction);
            controller.setViewModel(this.transactionEditViewModel);
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }
}
