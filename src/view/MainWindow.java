package view;

import com.cathive.fx.guice.FXMLController;
import com.cathive.fx.guice.GuiceFXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/31/14.
 */
@FXMLController
public class MainWindow implements Initializable {
    @Inject
    private GuiceFXMLLoader fxmlLoader;
    @FXML
    private Tab accountsTab;
    @FXML
    private Tab transactionTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.accountsTab
                    .setContent(
                            fxmlLoader
                                    .load(getClass().getResource("account/AccountView.fxml"))
                                    .getRoot());
            this.transactionTab
                    .setContent(
                            fxmlLoader
                                    .load(getClass().getResource("transaction/TransactionView.fxml"))
                                    .getRoot());
        } catch (IOException e) {
            //TODO: Log something
            e.printStackTrace();
        }
    }
}
