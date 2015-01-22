package view.transaction;

import com.cathive.fx.guice.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Transaction;
import viewmodel.transaction.TransactionListingViewModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/31/14.
 */
@FXMLController
public class TransactionListingView implements Initializable {

    @FXML
    public ListView<Transaction> transactionList;
    @Inject
    @Named("i18n-resources")
    private ResourceBundle resources;
    @Inject
    private TransactionListingViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.transactionList.itemsProperty().bind(this.viewModel.getTransactionListProperty());
        this.transactionList.setCellFactory(callback -> new TransactionListCell(this.resources));
    }

    @FXML
    public void handleClick(final MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            final Transaction transaction = this.transactionList.getSelectionModel().getSelectedItem();
            this.viewModel.editTransaction(transaction);
        }
    }

    @FXML
    public void addTransaction(final ActionEvent actionEvent) {
        this.viewModel.addTransaction();
    }
}
